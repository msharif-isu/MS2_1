package harmonize.ErrorHandling.Exceptions;

public class UserAlreadyInvitedException extends RuntimeException {
    public UserAlreadyInvitedException(String user1, String user2) {
        super(user1 + " already sent friend invite to " + user2);
    }
}
