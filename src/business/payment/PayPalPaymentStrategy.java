/* File Name: PayPalPaymentStrategy.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package business.payment;

// Concrete Strategy for PayPal payments
public class PayPalPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean pay(double amount, PaymentDetails details) {
        // For PayPal simulation, require payer name (or email) present
        if (details == null) return false;
        String name = details.getName();
        if (name == null || name.trim().isEmpty()) return false;
        // Simulate external PayPal processing here
        return true;
    }
}
