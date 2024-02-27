package harmonize.ErrorHandling.Exceptions;

public class UserAlreadySentInviteException extends RuntimeException {
    public UserAlreadySentInviteException(String user) {
        super("You are already friends invite to user: " + user);
    }
}
