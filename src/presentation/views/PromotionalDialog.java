/* File Name: PromotionalDialog.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package presentation.views;

import javax.swing.*;
import java.awt.*;

// dialog box that displays monthly promotional news for customers
public class PromotionalDialog extends JDialog {
    private boolean acknowledged = false;

    public PromotionalDialog(Window owner) {
        super(owner, "Monthly Promotions", ModalityType.APPLICATION_MODAL);
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel title = new JLabel("December's Promotions and News");
        title.setFont(new Font("Tahoma", Font.BOLD, 18));
        root.add(title, BorderLayout.NORTH);

        String html = "<html>"
                + "<p><b>Double Miles:</b> Earn double loyalty miles on international flights.</p>"
                + "<p><b>Family Discount:</b> Kids under 6 fly free on domestic flights.</p>"
                + "<p><b>Holiday Route:</b> New flight added between Toronto and Calgary.</p>"
                + "<p></p>"
                + "<p>Book before January 1st to take advantage of these offers!</p>"
                + "</html>";

        JEditorPane content = new JEditorPane("text/html", html);
        content.setEditable(false);
        content.setBackground(root.getBackground());
        JScrollPane scroll = new JScrollPane(content);
        scroll.setPreferredSize(new Dimension(480, 180));
        root.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JCheckBox dontShow = new JCheckBox("Don't show this again");
        JButton ok = new JButton("OK");
        ok.addActionListener(e -> {
            acknowledged = true;
            setVisible(false);
        });
        bottom.add(dontShow);
        bottom.add(ok);
        root.add(bottom, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // Shows the dialog and returns true if the user acknowledged it (pressed OK)
    public boolean showDialog() {
        setVisible(true);
        return acknowledged;
    }
}
