package presentation.views;

import javax.swing.*;
import java.awt.*;

// MainPanel: shell that hosts a header (with sign-out) and a content area for role-specific views
public class MainPanel extends JPanel {
    private JLabel titleLabel;
    private JPanel contentHolder;
    private String username;
    private String role;

    public MainPanel(String username, String role, JPanel initialContent) {
        this.username = username;
        this.role = role;
        setLayout(new BorderLayout());
        initHeader();
        contentHolder = new JPanel(new BorderLayout());
        add(contentHolder, BorderLayout.CENTER);
        if (initialContent != null) setContent(initialContent);
    }

    private void initHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        titleLabel = new JLabel("User: " + username + " (" + role + ")");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(titleLabel, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton signOut = new JButton("Sign out");
        signOut.addActionListener(e -> signOut());
        right.add(signOut);
        header.add(right, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    public void setContent(JPanel view) {
        contentHolder.removeAll();
        contentHolder.add(view, BorderLayout.CENTER);
        contentHolder.revalidate();
        contentHolder.repaint();
    }

    private void signOut() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) w.dispose();
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
