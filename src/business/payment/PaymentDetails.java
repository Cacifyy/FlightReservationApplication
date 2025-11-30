package business.payment;

public class PaymentDetails {
    private String name;
    private String cardNumber;
    private String expiry;
    private String cvv;
    private String password; // for PayPal
    private String currency; // transaction currency

    public PaymentDetails(String name, String cardNumber, String expiry, String cvv) {
        this(name, cardNumber, expiry, cvv, null, null);
    }

    public PaymentDetails(String name, String cardNumber, String expiry, String cvv, String password, String currency) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
        this.password = password;
        this.currency = currency;
    }

    public String getName() { return name; }
    public String getCardNumber() { return cardNumber; }
    public String getExpiry() { return expiry; }
    public String getCvv() { return cvv; }
    public String getPassword() { return password; }
    public String getCurrency() { return currency; }
}
