package harmonize.ErrorHandling.Exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(int id) {
        super("Cannot find role with id: " + id);
    }

    public RoleNotFoundException(String name) {
        super("Cannot find role with name: " + name);
    }
}
