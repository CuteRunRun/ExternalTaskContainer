package yp.externaltaskcontainer.application;


import org.springframework.stereotype.Service;
import yp.externaltaskcontainer.application.exception.TaskResourcesNotEnough;
import yp.externaltaskcontainer.model.ExternalTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TaskManager {
    private final int nThreads = 5;
    private final ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

    public void createNewTask(ExternalTask task) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("start task");
            }
        });

    }
}
