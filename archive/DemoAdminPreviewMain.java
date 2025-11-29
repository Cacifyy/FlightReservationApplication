package presentation;

import presentation.views.SystemAdminPanelPreview;
import javax.swing.*;

public class DemoAdminPreviewMain {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Admin Panel Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            frame.add(new SystemAdminPanelPreview());
            frame.setVisible(true);
        });
    }
}
