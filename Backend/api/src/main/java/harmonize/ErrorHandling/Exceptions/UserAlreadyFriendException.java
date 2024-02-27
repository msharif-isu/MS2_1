package harmonize.ErrorHandling.Exceptions;

public class UserAlreadyFriendException extends RuntimeException {
    public UserAlreadyFriendException(String user) {
        super("You are already friends with user: " + user);
    }
}
