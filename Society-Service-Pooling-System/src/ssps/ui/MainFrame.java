package ssps.ui;
import ssps.service.PoolingService;
import javax.swing.*;
import java.awt.*;
public class MainFrame extends JFrame {
    private VisitPanel visits;
    private BookingPanel bookings;
    private LedgerPanel ledger;
    public MainFrame(PoolingService service) {
        super("Society Service Pooling System (SSPS) - College Prototype");
        setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(1280, 760); setLocationRelativeTo(null);
        ProfilePanel profile = new ProfilePanel(this::refreshAll); add(profile, BorderLayout.NORTH);
        visits = new VisitPanel(service, profile, this::refreshAll);
        bookings = new BookingPanel(service, profile, this::refreshAll); ledger = new LedgerPanel(service);
        JTabbedPane tabs = new JTabbedPane(); tabs.addTab("1. Nearby Visits", visits); tabs.addTab("2. Bills and Payments", bookings);
        tabs.addTab("3. Provider Ledger", ledger); add(tabs); refreshAll();
    }
    private void refreshAll() {
        if (visits != null) visits.refresh(); if (bookings != null) bookings.refresh(); if (ledger != null) ledger.refresh();
    }
}
