package presentation.views;

import javax.swing.*;
import java.awt.*;

// Simple modal dialog to simulate a payment flow (no backend)
public class PaymentDialog extends JDialog {
    private boolean success = false;
    private double amount;

    public PaymentDialog(Window owner, double amount) {
        super(owner, "Payment", ModalityType.APPLICATION_MODAL);
        this.amount = amount;
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel p = new JPanel(new BorderLayout(8,8));
        JPanel fields = new JPanel(new GridLayout(0,2,6,6));

        JTextField nameField = new JTextField(20);
        JTextField cardField = new JTextField(20);
        JTextField expiryField = new JTextField(6);
        JTextField cvvField = new JTextField(4);

        fields.add(new JLabel("Name on card:")); fields.add(nameField);
        fields.add(new JLabel("Card number:")); fields.add(cardField);
        fields.add(new JLabel("Expiry (MM/YY):")); fields.add(expiryField);
        fields.add(new JLabel("CVV:")); fields.add(cvvField);

        p.add(fields, BorderLayout.CENTER);

        JLabel amt = new JLabel(String.format("Amount: $%.2f", amount));
        amt.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(amt);
        p.add(top, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton payBtn = new JButton("Pay");
        JButton cancelBtn = new JButton("Cancel");
        buttons.add(cancelBtn);
        buttons.add(payBtn);
        p.add(buttons, BorderLayout.SOUTH);

        payBtn.addActionListener(e -> {
            // basic validation
            String name = nameField.getText().trim();
            String card = cardField.getText().trim().replaceAll("\\s+","");
            String exp = expiryField.getText().trim();
            String cvv = cvvField.getText().trim();
            if (name.isEmpty() || card.length() < 12 || exp.length() < 4 || cvv.length() < 3) {
                JOptionPane.showMessageDialog(this, "Please enter valid payment details.", "Invalid", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Simulate processing
            payBtn.setEnabled(false);
            cancelBtn.setEnabled(false);
            try {
                Thread.sleep(600); // simulate delay
            } catch (InterruptedException interruptedException) {
                // ignore
            }
            success = true; // always succeed in this simulation
            JOptionPane.showMessageDialog(this, "Payment successful.", "Paid", JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
        });

        cancelBtn.addActionListener(e -> {
            success = false;
            setVisible(false);
        });

        setContentPane(p);
    }

    // Show dialog and return whether payment succeeded
    public boolean showDialog() {
        setVisible(true);
        return success;
    }
}
