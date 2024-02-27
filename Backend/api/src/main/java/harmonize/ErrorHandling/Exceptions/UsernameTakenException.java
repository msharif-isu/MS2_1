package harmonize.ErrorHandling.Exceptions;

public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException(String username) {
        super(username + " is already taken.");
    }
}
