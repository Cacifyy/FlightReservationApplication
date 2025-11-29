package business.entities.payment;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Payment entity class representing a payment in the flight reservation system
 * Matches the payments table in the database schema
 */
public class Payment {
    
    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, PAYPAL, CASH
    }
    
    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }
    
    private int paymentId;
    private int reservationId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private PaymentMethod paymentMethod;
    private String cardNumber;
    private PaymentStatus status;
    private String confirmationCode;
    
    // Default constructor
    public Payment() {
        this.paymentDate = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
        this.confirmationCode = generateConfirmationCode();
    }
    
    // Constructor for new payment (without IDs)
    public Payment(int reservationId, BigDecimal amount, PaymentMethod paymentMethod) {
        this();
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }
    
    // Constructor for new payment with card number
    public Payment(int reservationId, BigDecimal amount, PaymentMethod paymentMethod, String cardNumber) {
        this(reservationId, amount, paymentMethod);
        this.cardNumber = cardNumber;
    }
    
    // Full constructor (for database loading)
    public Payment(int paymentId, int reservationId, BigDecimal amount, 
                   LocalDateTime paymentDate, PaymentMethod paymentMethod, 
                   String cardNumber, PaymentStatus status, String confirmationCode) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.cardNumber = cardNumber;
        this.status = status;
        this.confirmationCode = confirmationCode != null ? confirmationCode : generateConfirmationCode();
    }
    
    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
    
    public int getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public PaymentStatus getStatus() {
        return status;
    }
    
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    
    public String getConfirmationCode() {
        return confirmationCode;
    }
    
    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
    
    // Business methods
    public boolean isPending() {
        return status == PaymentStatus.PENDING;
    }
    
    public boolean isCompleted() {
        return status == PaymentStatus.COMPLETED;
    }
    
    public boolean isFailed() {
        return status == PaymentStatus.FAILED;
    }
    
    public boolean isRefunded() {
        return status == PaymentStatus.REFUNDED;
    }
    
    public boolean canBeRefunded() {
        return status == PaymentStatus.COMPLETED;
    }
    
    public boolean requiresCard() {
        return paymentMethod == PaymentMethod.CREDIT_CARD || 
               paymentMethod == PaymentMethod.DEBIT_CARD;
    }
    
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return null;
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
    
    public void processPayment() {
        if (status == PaymentStatus.PENDING) {
            // Simulate payment processing
            if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
                this.status = PaymentStatus.COMPLETED;
                this.paymentDate = LocalDateTime.now();
            } else {
                this.status = PaymentStatus.FAILED;
            }
        }
    }
    
    public void refundPayment() {
        if (canBeRefunded()) {
            this.status = PaymentStatus.REFUNDED;
        }
    }
    
    public void failPayment() {
        if (status == PaymentStatus.PENDING) {
            this.status = PaymentStatus.FAILED;
        }
    }
    
    private String generateConfirmationCode() {
        return "PAY" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public double getAmountAsDouble() {
        return amount != null ? amount.doubleValue() : 0.0;
    }
    
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", reservationId=" + reservationId +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", paymentMethod=" + paymentMethod +
                ", cardNumber='" + getMaskedCardNumber() + '\'' +
                ", status=" + status +
                ", confirmationCode='" + confirmationCode + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Payment payment = (Payment) obj;
        return paymentId == payment.paymentId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(paymentId);
    }
}