package ssps;
import ssps.service.PoolingService;
import ssps.storage.FileStorage;
import ssps.ui.MainFrame;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new MainFrame(new PoolingService(new FileStorage())).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "SSPS cannot start or load saved data. No saved data was overwritten.\n" + ex + "\nBack up data/ssps.dat before troubleshooting.", "SSPS Startup Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
