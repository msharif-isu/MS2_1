package UserInfo;

/**
 * class to create a user, used to populate user_item
 */
public class User {

    private String username;
    private int id;
    private String firstName;
    private String lastName;

    /**
     * creates a user
     * @param id
     * @param username
     */
    public User(int id, String username) {

        this.id = id;
        this.username = username;

    }

    /**
     * returns username
     * @return
     */
    public String getUsername() {

        return username;

    }

    /**
     * returns ID
     * @return
     */
    public int getId() {

        return id;

    }

}
