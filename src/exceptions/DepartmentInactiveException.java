package exceptions;

public class DepartmentInactiveException extends RuntimeException {
    public DepartmentInactiveException(String message) {
        super(message);
    }
}
