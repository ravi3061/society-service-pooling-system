package ssps.ui;
import ssps.service.*;
import javax.swing.*;
import java.awt.*;
public class LedgerPanel extends JPanel {
    private final PoolingService service;
    private final ReportService reports = new ReportService();
    private final JTextArea text = new JTextArea();
    public LedgerPanel(PoolingService service) {
        this.service = service; setLayout(new BorderLayout()); text.setEditable(false);
        text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13)); add(new JScrollPane(text));
    }
    public void refresh() { text.setText(reports.providerLedger(service)); }
}
