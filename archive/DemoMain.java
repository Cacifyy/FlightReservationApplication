package presentation;

import presentation.views.CustomerPanelPreview;
import javax.swing.*;

// Demo launcher to preview the CustomerPanel UI without database/auth
public class DemoMain {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Customer Panel Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);

            // Provide a sample username and a dummy customer id
            CustomerPanelPreview preview = new CustomerPanelPreview("john_doe", 3);
            frame.add(preview);
            frame.setVisible(true);
        });
    }
}
