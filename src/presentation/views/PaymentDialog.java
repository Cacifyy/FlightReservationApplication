package presentation.views;

import business.payment.PaymentDetails;
import business.payment.PaymentStrategy;
import business.payment.CreditCardPaymentStrategy;
import business.payment.PayPalPaymentStrategy;
import business.payment.CashPaymentStrategy;
import javax.swing.*;
import java.awt.*;

// Simple modal dialog to simulate a payment flow (no backend)
public class PaymentDialog extends JDialog {
    private boolean success = false;
    private double amount;
    private PaymentStrategy strategy;

    // Backwards-compatible constructor: defaults to CreditCardPaymentStrategy
    public PaymentDialog(Window owner, double amount) {
        this(owner, amount, new CreditCardPaymentStrategy());
    }

    public PaymentDialog(Window owner, double amount, PaymentStrategy strategy) {
        super(owner, "Payment", ModalityType.APPLICATION_MODAL);
        this.amount = amount;
        this.strategy = strategy;
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel p = new JPanel(new BorderLayout(8,8));
        // content area will switch between different payment method panels
        JPanel contentHolder = new JPanel(new CardLayout());

        JPanel cardPanel = new JPanel(new GridLayout(0,2,6,6));
        JPanel paypalPanel = new JPanel(new GridLayout(0,2,6,6));
        JPanel cashPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Payment method selector
        String[] methods = new String[]{"Credit Card", "PayPal", "Cash"};
        JComboBox<String> methodBox = new JComboBox<>(methods);
        // set default selection based on provided strategy
        if (strategy instanceof PayPalPaymentStrategy) methodBox.setSelectedItem("PayPal");
        else if (strategy instanceof CashPaymentStrategy) methodBox.setSelectedItem("Cash");
        else methodBox.setSelectedItem("Credit Card");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(methodBox, BorderLayout.WEST);

        // --- Credit Card panel components ---
        JTextField ccNameField = new JTextField(20);
        JTextField cardField = new JTextField(20);
        JTextField expiryField = new JTextField(6);
        JTextField cvvField = new JTextField(4);
        cardPanel.add(new JLabel("Name on card:")); cardPanel.add(ccNameField);
        cardPanel.add(new JLabel("Card number:")); cardPanel.add(cardField);
        cardPanel.add(new JLabel("Expiry (MM/YY):")); cardPanel.add(expiryField);
        cardPanel.add(new JLabel("CVV:")); cardPanel.add(cvvField);

        // --- PayPal panel components ---
        JTextField ppAccountField = new JTextField(20); // email or phone
        JPasswordField ppPasswordField = new JPasswordField(20);
        JLabel ppAmountLabel = new JLabel(String.format("Amount: %.2f", amount));
        JLabel ppCurrencyLabel = new JLabel("CAD");
        paypalPanel.add(new JLabel("PayPal account:")); paypalPanel.add(ppAccountField);
        paypalPanel.add(new JLabel("Password:")); paypalPanel.add(ppPasswordField);
        paypalPanel.add(new JLabel("Transaction amount:")); paypalPanel.add(ppAmountLabel);
        paypalPanel.add(new JLabel("Currency:")); paypalPanel.add(ppCurrencyLabel);

        // --- Cash panel components ---
        JTextField cashBookingNameField = new JTextField(20);
        cashBookingNameField.setPreferredSize(new java.awt.Dimension(240, 24));
        cashPanel.add(new JLabel("Booking name:"));
        cashPanel.add(cashBookingNameField);

        // add panels to content holder
        contentHolder.add(cardPanel, "CARD");
        contentHolder.add(paypalPanel, "PAYPAL");
        contentHolder.add(cashPanel, "CASH");

        p.add(contentHolder, BorderLayout.CENTER);

        JLabel amt = new JLabel(String.format("Amount: $%.2f", amount));
        amt.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel amtPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        amtPanel.add(amt);
        JPanel northWrapper = new JPanel(new BorderLayout());
        northWrapper.add(topPanel, BorderLayout.WEST);
        northWrapper.add(amtPanel, BorderLayout.CENTER);
        p.add(northWrapper, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton payBtn = new JButton("Pay");
        JButton cancelBtn = new JButton("Cancel");
        buttons.add(cancelBtn);
        buttons.add(payBtn);
        p.add(buttons, BorderLayout.SOUTH);

        // update strategy and visible panel when selection changes
        methodBox.addActionListener(e -> {
            String sel = (String) methodBox.getSelectedItem();
            CardLayout cl = (CardLayout) (contentHolder.getLayout());
            switch (sel) {
                case "PayPal":
                    strategy = new PayPalPaymentStrategy();
                    cl.show(contentHolder, "PAYPAL");
                    break;
                case "Cash":
                    strategy = new CashPaymentStrategy();
                    cl.show(contentHolder, "CASH");
                    break;
                default:
                    strategy = new CreditCardPaymentStrategy();
                    cl.show(contentHolder, "CARD");
                    break;
            }
        });

        payBtn.addActionListener(e -> {
            // basic validation and collect details based on selected method
            String sel = (String) methodBox.getSelectedItem();
            String ccName = null, card = null, exp = null, cvv = null, ppAccount = null, ppPassword = null, cashBookingName = null, currency = null;
            if ("Credit Card".equals(sel)) {
                ccName = ccNameField.getText().trim();
                card = cardField.getText().trim().replaceAll("\\s+","");
                exp = expiryField.getText().trim();
                cvv = cvvField.getText().trim();
                if (ccName.isEmpty() || card.length() < 12 || exp.length() < 4 || cvv.length() < 3) {
                    JOptionPane.showMessageDialog(this, "Please enter valid credit card details.", "Invalid", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else if ("PayPal".equals(sel)) {
                ppAccount = ppAccountField.getText().trim();
                ppPassword = new String(ppPasswordField.getPassword()).trim();
                currency = ppCurrencyLabel.getText();
                if (ppAccount.isEmpty() || ppPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter your PayPal account and password.", "Invalid", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else if ("Cash".equals(sel)) {
                cashBookingName = cashBookingNameField.getText().trim();
                if (cashBookingName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter your Booking name.", "Invalid", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Simulate processing
            payBtn.setEnabled(false);
            cancelBtn.setEnabled(false);
            try {
                Thread.sleep(600); // simulate delay
            } catch (InterruptedException interruptedException) {
                // ignore
            }

            PaymentDetails details;
            if ("Credit Card".equals(sel)) {
                details = new PaymentDetails(ccName, card, exp, cvv);
            } else if ("PayPal".equals(sel)) {
                details = new PaymentDetails(ppAccount, ppPassword, null, null, ppPassword, currency);
            } else { // Cash
                details = new PaymentDetails(cashBookingName, null, null, null);
            }

            boolean ok = (strategy != null) ? strategy.pay(amount, details) : true;

            success = ok;
            if (ok) {
                JOptionPane.showMessageDialog(this, "Payment successful.", "Paid", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Payment failed.", "Failed", JOptionPane.ERROR_MESSAGE);
            }
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
