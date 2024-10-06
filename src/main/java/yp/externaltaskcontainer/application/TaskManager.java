package yp.externaltaskcontainer.application;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yp.externaltaskcontainer.application.exception.*;
import yp.externaltaskcontainer.application.utils.TaskThreadExecutor;
import yp.externaltaskcontainer.application.utils.TaskThreadFactory;
import yp.externaltaskcontainer.model.ExternalTask;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TaskManager {
    @Value("${task.number}")
    private int maxThreadsNum;

    @Value("${task.waitTime}")
    private int waitSeconds;
    private ThreadPoolExecutor executor;

    @PostConstruct
    public void init() {
        executor = new TaskThreadExecutor(
                maxThreadsNum,                      // Core pool size (number of threads)
                maxThreadsNum,                      // Maximum pool size
                0L, TimeUnit.MILLISECONDS, // Keep-alive time (not relevant for a fixed pool)
                new SynchronousQueue<>(), // No queue: tasks will be rejected if no threads are available
                new ThreadPoolExecutor.AbortPolicy() // Rejection policy: throw exception
        );
//        executor.setThreadFactory(new TaskThreadFactory());
    }

    public int getExecutionTaskNumber() {
        return executor.getActiveCount();
    }

    public void createNewTask(ExternalTask task) {
        try {
            executor.submit(() -> {
                System.out.println("start task");
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.command(task.getCommand());
                try {
                    Process newTask = processBuilder.start();
                    boolean isFinished = newTask.waitFor(waitSeconds, TimeUnit.SECONDS);
                    if (!isFinished) {
                        System.out.println("Task exceeded time limit, terminating...");
                        newTask.destroy();
                        throw new TaskOverTimeException("Task exceeded time limit");
                    }
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
