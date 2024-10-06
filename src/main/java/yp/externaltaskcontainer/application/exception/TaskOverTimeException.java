package yp.externaltaskcontainer.application.exception;

public class TaskOverTimeException extends RuntimeException {
    public TaskOverTimeException() {
        super();
    }

    public TaskOverTimeException(String message) {
        super(message);
    }

    public TaskOverTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskOverTimeException(Throwable cause) {
        super(cause);
    }

}
