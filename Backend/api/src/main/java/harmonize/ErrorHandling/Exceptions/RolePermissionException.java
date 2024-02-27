package harmonize.ErrorHandling.Exceptions;

public class RolePermissionException extends RuntimeException {
    public RolePermissionException(String role) {
        super("Conflict with role: " + role);
    }
}
