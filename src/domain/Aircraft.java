
public class Aircraft {

    private Long id;
    private String model;
    private int capacity;

    public Aircraft() { }

    public Aircraft(Long id, String model, int capacity) {
        this.id = id;
        this.model = model;
        this.capacity = capacity;
    }
}