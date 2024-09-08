package yp.externaltaskcontainer.application.exception;

public class TaskInterruptionException extends RuntimeException {
    public TaskInterruptionException() {
        super();
    }

    public TaskInterruptionException(String message) {
        super(message);
    }

    public TaskInterruptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskInterruptionException(Throwable cause) {
        super(cause);
    }

}
