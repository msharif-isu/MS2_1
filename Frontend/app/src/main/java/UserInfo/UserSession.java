package UserInfo;

/**
 * Class which holds data about the current user using the application
 */
public class UserSession {
    private static UserSession instance;
    private Member currentUser;

    private String jwtToken, password;

    private UserSession() {

    }

    /**
     * Get the instance of the user session
     * @return
     */
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public Member getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Member currentUser) {
        this.currentUser = currentUser;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setPassword(String password) {
        this.password = password;
        }

    public String getPassword() {
        return password;
    }
}
