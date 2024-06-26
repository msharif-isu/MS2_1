package UserInfo;

/**
 * Class which holds data for any member who is a part of the application
 */
public class Member {

    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String bio;

    /**
     * Constructor for the member
     * @param id
     * @param firstName
     * @param lastName
     * @param username
     * @param bio
     */
    public Member(int id, String firstName, String lastName, String username, String bio) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.bio = bio;
    }

    // Getters
    public int getid() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }
}
