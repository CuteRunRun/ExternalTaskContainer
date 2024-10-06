package yp.externaltaskcontainer.application;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import yp.externaltaskcontainer.application.exception.TaskOverTimeException;
import yp.externaltaskcontainer.application.exception.TaskResourcesNotEnough;
import yp.externaltaskcontainer.model.ExternalTask;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class TaskManagerTest {
    @Value("${task.number}")
    private int maxThreadsNum;

    @Autowired
    TaskManager taskManager;

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setErr(originalErr);
    }

    @SneakyThrows
    @Test
    @Order(1)
    void should_terminate_task_and_throw_exception_when_task_is_overtime() {
        // Given
        ExternalTask randomTask = new ExternalTask();
        randomTask.setCommand(new ArrayList<>() {{
            add("bash");
            add("-c");
            add("sleep 2");
        }});

        // When
        taskManager.createNewTask(randomTask);
        TimeUnit.SECONDS.sleep(2);
        String expectedMessage = "Task exceeded time limit";
        assertEquals(expectedMessage + System.lineSeparator(), errContent.toString());
    }

    @Test
    @Order(2)
    void should_get_executed_task_number() {
        // Given
        ExternalTask randomTask = new ExternalTask();
        randomTask.setCommand(new ArrayList<>() {{
            add("bash");
            add("-c");
            add("sleep 1");
        }});

        for (int i = 0; i < maxThreadsNum; i++) {
            taskManager.createNewTask(randomTask);
        }

        assertEquals(taskManager.getExecutionTaskNumber(), maxThreadsNum);
    }

    @SneakyThrows
    @Test
    @Order(3)
    void should_throw_exception_when_thread_number_exceeded() {
        // Given
        ExternalTask randomTask = new ExternalTask();
        randomTask.setCommand(new ArrayList<>() {{
            add("bash");
            add("-c");
            add("sleep 2");
        }});
        TimeUnit.SECONDS.sleep(2);
        for (int i = 0; i < maxThreadsNum; i++) {
            taskManager.createNewTask(randomTask);
        }


        // When and then
        assertThrows(TaskResourcesNotEnough.class, () -> {
            taskManager.createNewTask(randomTask);
        });
    }
}