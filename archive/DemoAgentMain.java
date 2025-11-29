package presentation;

import presentation.views.AgentPanel;
import javax.swing.*;

// Demo launcher for flight agent preview
public class DemoAgentMain {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Flight Agent Panel Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);

            AgentPanel panel = new AgentPanel("agent1");
            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
