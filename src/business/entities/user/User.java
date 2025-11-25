public class User {

    private Long id;
    private String username;
    private String passwordHash;
    private UserRole role; // CUSTOMER, AGENT, ADMIN

    public User() { }

    public User(Long id, String username, String passwordHash, UserRole role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}