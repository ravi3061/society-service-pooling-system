package ssps.ui;
import ssps.model.*;
import ssps.service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class VisitPanel extends JPanel {
    private final PoolingService service;
    private final ProfilePanel profile;
    private final Runnable refreshAll;
    private final DefaultTableModel model = UiUtils.model("ID", "Provider", "Type", "Skill", "Zone", "Date / Slot", "Jobs / Capacity", "Status");
    private final JTable table = new JTable(model);
    private final List<String> ids = new ArrayList<>();
    public VisitPanel(PoolingService service, ProfilePanel profile, Runnable refreshAll) {
        this.service = service; this.profile = profile; this.refreshAll = refreshAll;
        setLayout(new BorderLayout()); table.setAutoCreateRowSorter(true); table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table)); JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(button("Create Visit", this::create)); buttons.add(button("Confirm Provider [Admin]", this::approve));
        buttons.add(button("Join Visit", this::join)); buttons.add(button("Lock Rates [Admin]", this::lock));
        buttons.add(button("Record Payout [Admin]", this::payout)); add(buttons, BorderLayout.SOUTH);
    }
    private JButton button(String text, UiUtils.Action action) { return UiUtils.button(text, this, action, refreshAll); }
    private Visit selected() {
        int row = table.getSelectedRow(); PoolingService.require(row >= 0, "Select a visit first.");
        return service.visit(ids.get(table.convertRowIndexToModel(row)));
    }
    private void create() throws Exception {
        String owner = profile.resident(); profile.flat();
        JTextField name = new JTextField(), contact = new JTextField("Not available");
        JTextField date = new JTextField(LocalDate.now().plusDays(1).toString()), slot = new JTextField("11 AM - 2 PM");
        JComboBox<String> type = new JComboBox<>(new String[]{"Independent", "Agency", "Assisted / Offline"});
        JComboBox<String> skill = new JComboBox<>(new String[]{"Electrician", "AC Service", "Plumber", "Cleaner", "Labour / Helper"});
        JSpinner capacity = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1)), discount = new JSpinner(new SpinnerNumberModel(10, 0, 30, 1));
        if (!UiUtils.formDialog(this, "Create visit - provider confirmation pending", UiUtils.form("Provider", name, "Type", type, "Contact (optional)", contact,
                "Skill", skill, "Date YYYY-MM-DD", date, "Time slot", slot, "Job capacity", capacity, "Proposed labour discount % (2+ jobs)", discount))) return;
        Visit v = new Visit(); v.provider = UiUtils.text(name); v.type = (String) type.getSelectedItem(); v.skill = (String) skill.getSelectedItem();
        v.organiser = owner; v.contact = contact.getText().trim(); v.zone = profile.zone(); v.date = UiUtils.text(date); v.slot = UiUtils.text(slot);
        v.capacity = (Integer) capacity.getValue(); v.discount = (Integer) discount.getValue(); service.create(v);
    }
    private void approve() throws Exception {
        Visit v = selected();
        if (UiUtils.yes(this, "Has the provider agreed to the scope, capacity, discount and 5% commission?\nAn organiser may record offline consent. No worker login is required.")) service.approve(v.id);
    }
    private void join() throws Exception {
        Visit v = selected(); String name = profile.resident(), flat = profile.flat();
        JTextField work = new JTextField(), base = new JTextField("500.00");
        if (UiUtils.formDialog(this, "Join visit - estimated labour charges only", UiUtils.form("Work description", work, "Estimated labour INR", base)))
            service.join(v.id, name, flat, UiUtils.text(work), BillingService.parse(base.getText()));
    }
    private void lock() throws Exception {
        Visit v = selected(); List<Booking> bookings = service.members(v.id);
        PoolingService.require(v.status.equals("OPEN") && !bookings.isEmpty(), "Select an OPEN visit with bookings.");
        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8)); List<JTextField> inputs = new ArrayList<>();
        for (Booking b : bookings) {
            JTextField field = new JTextField(BillingService.display(b.base)); inputs.add(field);
            p.add(new JLabel(b.resident + " - final labour INR")); p.add(field);
        }
        p.add(new JLabel("Group discount: " + (bookings.size() >= 2 ? v.discount : 0) + "%; platform fee: 5%"));
        p.add(new JLabel("Confirm only AFTER every resident and provider agrees."));
        if (!UiUtils.formDialog(this, "Lock rates - no subsequent edits", p)) return;
        List<BigDecimal> prices = new ArrayList<>();
        for (JTextField field : inputs) prices.add(BillingService.parse(field.getText()));
        service.lock(v.id, prices);
    }
    private void payout() throws Exception {
        Visit v = selected();
        if (UiUtils.yes(this, "Record simulated settlement?\nDirect payments are NOT paid again.\nDirect-payment commission remains due and is not collected.")) service.payout(v.id);
    }
    public void refresh() {
        model.setRowCount(0); ids.clear();
        for (Visit v : service.visits()) if (v.zone.equals(profile.zone())) {
            ids.add(v.id); model.addRow(new Object[]{v.id, v.provider, v.type, v.skill, v.zone, v.date + " / " + v.slot, service.members(v.id).size() + " / " + v.capacity, v.status});
        }
    }
}
