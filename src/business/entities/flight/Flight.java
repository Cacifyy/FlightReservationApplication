import java.time.LocalDateTime;

public class Flight {

    private Long id;
    private String flightNumber;
    private Route route;
    private Aircraft aircraft;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double basePrice;

    public Flight() { }

    public Flight(Long id,
                  String flightNumber,
                  Route route,
                  Aircraft aircraft,
                  LocalDateTime departureTime,
                  LocalDateTime arrivalTime,
                  double basePrice) {

        this.id = id;
        this.flightNumber = flightNumber;
        this.route = route;
        this.aircraft = aircraft;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.basePrice = basePrice;
    }
}