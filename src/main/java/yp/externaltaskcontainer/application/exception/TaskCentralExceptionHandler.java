package yp.externaltaskcontainer.application.exception;

public class TaskCentralExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.err.println("Uncaught exception in thread " + t.getName() + ": " + e.getMessage());
        e.printStackTrace(); // Log the exception or handle it accordingly
    }
}
