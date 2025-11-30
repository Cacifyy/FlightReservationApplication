/* File Name: PaymentStrategy.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package business.payment;

// Strategy interface for payment methods
public interface PaymentStrategy {
    /**
     * Pay the specified amount using given details.
     * @param amount amount to pay
     * @param details payment details (card number, name, etc.)
     * @return true if payment succeeded
     */
    boolean pay(double amount, PaymentDetails details);
}
