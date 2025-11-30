package business.payment;

public class CashPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean pay(double amount, PaymentDetails details) {
        // Cash payments are handled outside this UI; for simulation assume success
        return true;
    }
}
