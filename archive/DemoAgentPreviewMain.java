package presentation;

import presentation.views.AgentPanelPreview;
import javax.swing.*;

// Demo launcher to preview the FlightAgentPanel UI without database/auth
public class DemoAgentPreviewMain {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Flight Agent Panel Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);

            AgentPanelPreview preview = new AgentPanelPreview("agent1", 3);
            frame.add(preview);
            frame.setVisible(true);
        });
    }
}
