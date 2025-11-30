package business.payment;

public interface PaymentStrategy {
    /**
     * Pay the specified amount using given details.
     * @param amount amount to pay
     * @param details payment details (card number, name, etc.)
     * @return true if payment succeeded
     */
    boolean pay(double amount, PaymentDetails details);
}
