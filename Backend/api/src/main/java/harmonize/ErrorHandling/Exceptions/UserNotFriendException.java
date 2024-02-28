package harmonize.ErrorHandling.Exceptions;

public class UserNotFriendException extends RuntimeException {
    public UserNotFriendException(String user1, String user2) {
        super(user1 + " is not friends with " + user2);
    }
}
