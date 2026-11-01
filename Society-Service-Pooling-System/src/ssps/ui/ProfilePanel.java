package ssps.ui;
import javax.swing.*;
import java.awt.*;
public class ProfilePanel extends JPanel {
    private final JTextField resident = new JTextField("Alex"), flat = new JTextField("Green Society / 101");
    private final JComboBox<String> zone = new JComboBox<>(new String[]{"Zone A", "Zone B", "Zone C"});
    public ProfilePanel(Runnable changed) {
        setLayout(new BorderLayout());
        add(UiUtils.form("Acting resident (demo only)", resident, "Society / flat", flat, "Current zone", zone));
        add(new JLabel(" SSPS DEMO: no secure login; admin actions are unrestricted. All payment transfers are simulated."), BorderLayout.SOUTH);
        zone.addActionListener(e -> changed.run());
    }
    public String resident() { return UiUtils.text(resident); }
    public String flat() { return UiUtils.text(flat); }
    public String zone() { return (String) zone.getSelectedItem(); }
}
