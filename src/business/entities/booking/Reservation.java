import java.time.LocalDateTime;

public class Reservation {

    private Long id;
    private Customer customer;
    private Flight flight;
    private String seatNumber;
    private LocalDateTime bookingDateTime;
    private Payment payment;

    public Reservation() { }

    public Reservation(Long id,
                       Customer customer,
                       Flight flight,
                       String seatNumber,
                       LocalDateTime bookingDateTime) {
        this.id = id;
        this.customer = customer;
        this.flight = flight;
        this.seatNumber = seatNumber;
        this.bookingDateTime = bookingDateTime;
    }
}