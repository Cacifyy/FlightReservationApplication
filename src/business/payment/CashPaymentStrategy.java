/* File Name: CashPaymentStrategy.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package business.payment;

// Concrete Strategy for cash payments
public class CashPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean pay(double amount, PaymentDetails details) {
        // Cash payments are handled outside this UI; for simulation assume success
        return true;
    }
}
