package business.payment;

public class CreditCardPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean pay(double amount, PaymentDetails details) {
        // Simple simulation: basic validation of card number and cvv
        if (details == null) return false;
        String card = details.getCardNumber();
        String cvv = details.getCvv();
        if (card == null || card.replaceAll("\\s+","").length() < 12) return false;
        if (cvv == null || cvv.length() < 3) return false;
        // Here you could call external payment gateway; we simulate success
        return true;
    }
}
