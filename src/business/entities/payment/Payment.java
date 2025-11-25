import java.time.LocalDateTime;

public class Payment {

    private Long id;
    private double amount;
    private LocalDateTime paymentDateTime;
    private String confirmationCode;

    public Payment() { }

    public Payment(Long id,
                   double amount,
                   LocalDateTime paymentDateTime,
                   String confirmationCode) {
        this.id = id;
        this.amount = amount;
        this.paymentDateTime = paymentDateTime;
        this.confirmationCode = confirmationCode;
    }
}