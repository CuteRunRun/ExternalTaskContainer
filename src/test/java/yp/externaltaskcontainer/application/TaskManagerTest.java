package yp.externaltaskcontainer.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.Task;
import org.springframework.test.context.ActiveProfiles;
import yp.externaltaskcontainer.application.exception.TaskResourcesNotEnough;
import yp.externaltaskcontainer.model.ExternalTask;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TaskManagerTest {
    @Value("${task.number}")
    private int maxThreadsNum;

    @Autowired
    TaskManager taskManager;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void should_throw_exception_when_thread_number_exceeded() {
        // Given
        ExternalTask randomTask = new ExternalTask();
        randomTask.setCommand(new ArrayList<>() {{
            add("bash");
            add("-c");
            add("sleep 5");
        }});

        for (int i = 0; i < maxThreadsNum; i++){
            taskManager.createNewTask(randomTask);
        }

        // When and then
        assertThrows(TaskResourcesNotEnough.class,()->{
            taskManager.createNewTask(randomTask);
        });
    }
}