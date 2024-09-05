package yp.externaltaskcontainer.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yp.externaltaskcontainer.model.ExternalTask;

class TaskManagerTest {
    TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new TaskManager();
    }

    @AfterEach
    void tearDown() {
    }
}