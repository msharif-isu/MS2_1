package harmonize.ErrorHandling.Exceptions;

public class UserFriendSelfException extends RuntimeException {
    public UserFriendSelfException(String username) {
        super(username + " cannot be friends with self.");
    }
}
