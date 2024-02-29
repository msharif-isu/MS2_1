package harmonize.ErrorHandling.Exceptions;

public class UserAlreadyFriendException extends RuntimeException {
    public UserAlreadyFriendException(String user1, String user2) {
        super(user1 + " is already sent friends with " + user2);
    }
}
