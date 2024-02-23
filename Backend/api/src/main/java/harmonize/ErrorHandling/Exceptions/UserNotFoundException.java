package harmonize.ErrorHandling.Exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super("Cannot find user with id: " + id);
    }

    public UserNotFoundException(String username) {
        super("Cannot find user with username: " + username);
    }
}
