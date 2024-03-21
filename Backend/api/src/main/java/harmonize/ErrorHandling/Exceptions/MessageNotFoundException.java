package harmonize.ErrorHandling.Exceptions;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(int id) {
        super("Cannot find message with id: " + id);
    }

    public MessageNotFoundException(String message) {
        super(message);
    }
}