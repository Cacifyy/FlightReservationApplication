
public class Route {

    private Long id;
    private String originAirportCode;
    private String destinationAirportCode;

    public Route() { }

    public Route(Long id, String originAirportCode, String destinationAirportCode) {
        this.id = id;
        this.originAirportCode = originAirportCode;
        this.destinationAirportCode = destinationAirportCode;
    }
}