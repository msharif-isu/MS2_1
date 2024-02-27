package harmonize.ErrorHandling.Exceptions;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException() {
        super("Cannot make changes on another user");
    }
}

