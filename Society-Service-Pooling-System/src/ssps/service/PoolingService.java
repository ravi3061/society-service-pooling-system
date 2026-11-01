package ssps.service;
import ssps.model.*;
import ssps.storage.FileStorage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
public class PoolingService {
    private AppData data;
    private final FileStorage storage;
    private final BillingService billing = new BillingService();
    public PoolingService(FileStorage storage) throws Exception { this.storage = storage; data = storage.load(); }
    @FunctionalInterface private interface Change { void run() throws Exception; }
    private void commit(Change change) throws Exception {
        byte[] backup = storage.snapshot(data);
        try { change.run(); storage.save(data); }
        catch (Exception ex) { data = storage.restore(backup); throw ex; }
    }
    public static void require(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }
    public List<Visit> visits() { return List.copyOf(data.visits); }
    public List<Booking> bookings() { return List.copyOf(data.bookings); }
    public Visit visit(String id) { return data.visits.stream().filter(v -> v.id.equals(id)).findFirst().orElseThrow(); }
    public Booking booking(String id) { return data.bookings.stream().filter(b -> b.id.equals(id)).findFirst().orElseThrow(); }
    public List<Booking> members(String id) {
        return data.bookings.stream().filter(b -> b.visitId.equals(id) && !b.status.equals("CANCELLED")).toList();
    }
    public void create(Visit v) throws Exception {
        require(!v.provider.isBlank() && !v.organiser.isBlank() && !v.slot.isBlank(), "Provider, organiser and time slot are required.");
        require(!LocalDate.parse(v.date).isBefore(LocalDate.now()), "Visit date cannot be in the past.");
        require(v.capacity >= 1 && v.capacity <= 20 && v.discount >= 0 && v.discount <= 30, "Invalid capacity or discount.");
        commit(() -> data.visits.add(v));
    }
    public void approve(String id) throws Exception {
        Visit v = visit(id); require(v.status.equals("PENDING"), "Only pending visits can be approved.");
        commit(() -> v.status = "OPEN");
    }
    public void join(String id, String resident, String flat, String work, BigDecimal base) throws Exception {
        Visit v = visit(id);
        require(v.status.equals("OPEN"), "Visit must be OPEN.");
        require(!LocalDate.parse(v.date).isBefore(LocalDate.now()), "Visit date has passed.");
        require(members(id).size() < v.capacity, "This visit is full.");
        require(!resident.isBlank() && !flat.isBlank() && !work.isBlank(), "Resident, flat and work description are required.");
        require(members(id).stream().noneMatch(b -> b.resident.equalsIgnoreCase(resident) && b.flat.equalsIgnoreCase(flat)), "This resident and flat have already joined.");
        Booking b = new Booking(); b.visitId = id; b.resident = resident; b.flat = flat; b.work = work; b.base = BillingService.parse(base.toPlainString());
        commit(() -> data.bookings.add(b));
    }
    public void cancel(String id) throws Exception {
        Booking b = booking(id);
        require(visit(b.visitId).status.equals("OPEN") && b.status.equals("JOINED"), "Cancellation is allowed only before rates are locked.");
        commit(() -> b.status = "CANCELLED");
    }
    public void lock(String id, List<BigDecimal> prices) throws Exception {
        Visit v = visit(id); List<Booking> list = members(id);
        require(v.status.equals("OPEN") && !list.isEmpty(), "An OPEN visit with at least one booking is required.");
        require(prices.size() == list.size(), "The price count does not match the booking count.");
        for (BigDecimal p : prices) BillingService.parse(p.toPlainString());
        commit(() -> {
            int discount = list.size() >= 2 ? v.discount : 0;
            for (int i = 0; i < list.size(); i++) { billing.calculate(list.get(i), prices.get(i), discount); list.get(i).status = "CONFIRMED"; }
            v.status = "LOCKED";
        });
    }
    public void complete(String id) throws Exception {
        Booking b = booking(id); require(b.status.equals("CONFIRMED"), "The booking must be CONFIRMED first.");
        commit(() -> {
            b.status = "COMPLETED";
            if (members(b.visitId).stream().allMatch(x -> x.status.equals("COMPLETED"))) visit(b.visitId).status = "COMPLETED";
        });
    }
    public void pay(String id, String mode) throws Exception {
        Booking b = booking(id); require(b.status.equals("COMPLETED"), "Complete the job before recording payment.");
        require(b.payment.equals("UNPAID"), "Payment is already recorded; duplicate collection is blocked.");
        require(List.of("PLATFORM DEMO", "DIRECT CASH", "DIRECT UPI").contains(mode), "Unknown payment mode.");
        commit(() -> b.payment = mode);
    }
    public void payout(String id) throws Exception {
        Visit v = visit(id); require(v.status.equals("COMPLETED"), "Complete all jobs first.");
        require(!v.payout, "Settlement is already recorded.");
        require(members(id).stream().noneMatch(b -> b.payment.equals("UNPAID")), "Record all resident payments first.");
        commit(() -> v.payout = true);
    }
}
