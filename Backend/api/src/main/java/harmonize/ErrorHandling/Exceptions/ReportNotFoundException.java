package harmonize.ErrorHandling.Exceptions;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(int id) {
        super("Cannot find report with id: " + id);
    }
}