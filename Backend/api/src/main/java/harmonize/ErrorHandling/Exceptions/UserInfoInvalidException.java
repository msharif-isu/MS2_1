package harmonize.ErrorHandling.Exceptions;

public class UserInfoInvalidException extends RuntimeException {
    public UserInfoInvalidException(String message) {
        super(message);
    }
}
