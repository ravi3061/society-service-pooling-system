package ssps.ui;
import ssps.model.*;
import ssps.service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
public class BookingPanel extends JPanel {
    private final PoolingService service;
    private final ProfilePanel profile;
    private final DefaultTableModel model = UiUtils.model("ID", "Resident / Flat", "Work", "Visit", "Status", "Base", "Discount", "Payable", "Fee 5%", "Worker Net", "Payment");
    private final JTable table = new JTable(model);
    private final List<String> ids = new ArrayList<>();
    public BookingPanel(PoolingService service, ProfilePanel profile, Runnable refresh) {
        this.service = service; this.profile = profile; setLayout(new BorderLayout());
        table.setAutoCreateRowSorter(true); table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); add(new JScrollPane(table));
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(UiUtils.button("Cancel Before Lock", this, () -> { Booking b = selected(); if (UiUtils.yes(this, "Cancel this booking?")) service.cancel(b.id); }, refresh));
        buttons.add(UiUtils.button("Confirm Job Completed", this, () -> { Booking b = selected(); if (UiUtils.yes(this, "Do the resident and worker confirm completion?")) service.complete(b.id); }, refresh));
        buttons.add(UiUtils.button("Record Payment", this, this::pay, refresh)); add(buttons, BorderLayout.SOUTH);
    }
    private Booking selected() {
        int row = table.getSelectedRow(); PoolingService.require(row >= 0, "Select a booking first.");
        return service.booking(ids.get(table.convertRowIndexToModel(row)));
    }
    private void pay() throws Exception {
        Booking b = selected();
        Object mode = JOptionPane.showInputDialog(this, "NO real money is transferred.\nFor direct payment, confirm that the worker received the full payable amount.", "Payment record", JOptionPane.PLAIN_MESSAGE, null,
                new String[]{"PLATFORM DEMO", "DIRECT CASH", "DIRECT UPI"}, "PLATFORM DEMO");
        if (mode != null) service.pay(b.id, mode.toString());
    }
    public void refresh() {
        model.setRowCount(0); ids.clear();
        for (Booking b : service.bookings()) if (service.visit(b.visitId).zone.equals(profile.zone())) {
            boolean priced = b.status.equals("CONFIRMED") || b.status.equals("COMPLETED"); ids.add(b.id);
            model.addRow(new Object[]{b.id, b.resident + " / " + b.flat, b.work, b.visitId, b.status, BillingService.display(b.base),
                priced ? BillingService.display(b.base.subtract(b.payable)) : "-", priced ? BillingService.display(b.payable) : "-",
                priced ? BillingService.display(b.fee) : "-", priced ? BillingService.display(b.payable.subtract(b.fee)) : "-", b.payment});
        }
    }
}
