package yp.externaltaskcontainer.application.exception;

public class TaskResourcesNotEnough extends RuntimeException {
    public TaskResourcesNotEnough() {
        super();
    }

    public TaskResourcesNotEnough(String message) {
        super(message);
    }

    public TaskResourcesNotEnough(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskResourcesNotEnough(Throwable cause) {
        super(cause);
    }
}
