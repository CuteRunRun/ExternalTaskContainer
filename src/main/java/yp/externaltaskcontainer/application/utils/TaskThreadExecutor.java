package yp.externaltaskcontainer.application.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskThreadExecutor extends ThreadPoolExecutor {

    public TaskThreadExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        // Catch exceptions that might have been thrown by the Runnable task
        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();  // This will rethrow the exception if the task failed
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();  // Get the cause of the exception
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();  // Reset interrupted flag
            }
        }

        if (t != null) {
            System.err.println(t.getMessage());
        }
    }
}
