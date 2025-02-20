package vn.edu.hcmuaf.fit.demohello.exception;

public class ResourceNotfoundException extends RuntimeException {
    public ResourceNotfoundException(String message) {
        super(message);
    }

    public ResourceNotfoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
