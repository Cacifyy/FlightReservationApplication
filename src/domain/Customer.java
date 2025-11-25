public class Customer {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;

    public Customer() { }

    public Customer(Long id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}