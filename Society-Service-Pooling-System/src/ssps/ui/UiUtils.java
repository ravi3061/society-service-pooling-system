package ssps.ui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
public class UiUtils {
    @FunctionalInterface public interface Action { void run() throws Exception; }
    public static JButton button(String label, Component parent, Action action, Runnable refresh) {
        JButton b = new JButton(label);
        b.addActionListener(e -> {
            try { action.run(); }
            catch (Exception ex) { JOptionPane.showMessageDialog(parent, ex.getMessage(), "Action not completed", JOptionPane.ERROR_MESSAGE); }
            finally { refresh.run(); }
        });
        return b;
    }
    public static DefaultTableModel model(String... columns) {
        return new DefaultTableModel(columns, 0) { public boolean isCellEditable(int r, int c) { return false; } };
    }
    public static JPanel form(Object... fields) {
        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        for (int i = 0; i < fields.length; i += 2) { p.add(new JLabel(fields[i].toString())); p.add((Component) fields[i + 1]); }
        return p;
    }
    public static boolean formDialog(Component parent, String title, JPanel form) {
        return JOptionPane.showConfirmDialog(parent, form, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
    }
    public static boolean yes(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    public static String text(JTextField field) {
        String value = field.getText().trim();
        if (value.isEmpty()) throw new IllegalArgumentException("Fill all required fields.");
        return value;
    }
}
