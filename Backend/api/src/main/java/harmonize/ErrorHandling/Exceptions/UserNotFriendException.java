package harmonize.ErrorHandling.Exceptions;

public class UserNotFriendException extends RuntimeException {
    public UserNotFriendException(String user) {
        super("You are not friends with user: " + user);
    }
}
