package com.universe.touchpoint.spring;

import com.universe.touchpoint.meta.data.TaskMeta;
import com.universe.touchpoint.spring.annotation.Task;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskMetaTest {

    @Autowired
    @Task("test")
    private TaskMeta taskMeta;

    @Test
    public void testTask() {
        assertEquals("test", taskMeta.getName());
    }

}
