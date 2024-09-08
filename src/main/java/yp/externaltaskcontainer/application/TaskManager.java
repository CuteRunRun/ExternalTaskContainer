package yp.externaltaskcontainer.application;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yp.externaltaskcontainer.application.exception.CommandExecutionException;
import yp.externaltaskcontainer.application.exception.TaskCentralExceptionHandler;
import yp.externaltaskcontainer.application.exception.TaskInterruptionException;
import yp.externaltaskcontainer.application.exception.TaskResourcesNotEnough;
import yp.externaltaskcontainer.model.ExternalTask;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TaskManager {
    @Value("${task.number}")
    private int maxThreadsNum;
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            5,                      // Core pool size (number of threads)
            5,                      // Maximum pool size
            0L, TimeUnit.MILLISECONDS, // Keep-alive time (not relevant for a fixed pool)
            new SynchronousQueue<>(), // No queue: tasks will be rejected if no threads are available
            new ThreadPoolExecutor.AbortPolicy() // Rejection policy: throw exception
    );

    public TaskManager() {
        TaskCentralExceptionHandler taskCentralExceptionHandler = new TaskCentralExceptionHandler();
        executor.setThreadFactory(new CustomThreadFactory(taskCentralExceptionHandler));
    }

    public void createNewTask(ExternalTask task) {
        try {
            executor.submit(() -> {
                System.out.println("start task");
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.command(task.getCommand());
                try {
                    Process newTask = processBuilder.start();
                    newTask.waitFor();
                } catch (IOException e) {
                    throw new CommandExecutionException(e);
                } catch (InterruptedException e) {
                    throw new TaskInterruptionException(e);
                }

            });
        } catch (RejectedExecutionException e) {
            throw new TaskResourcesNotEnough(String.format("Task resource is full. Max task number is %d.", maxThreadsNum));
        }
    }
}

class CustomThreadFactory implements ThreadFactory {
    private final String namePrefix = "ExternalTask-";
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final Thread.UncaughtExceptionHandler handler;

    public CustomThreadFactory(Thread.UncaughtExceptionHandler handler) {
        this.handler = handler;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, namePrefix + threadNumber.getAndIncrement());
        thread.setUncaughtExceptionHandler(handler); // Set the custom exception handler
        return thread;
    }
}