package com.universe.touchpoint.spring;

import static org.junit.jupiter.api.Assertions.*;

import com.universe.touchpoint.meta.data.TaskMeta;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TaskTestApplication.class)
public class MetricsCollectTaskMetaTest {

  @Autowired
  private TaskMeta taskMeta;

  @Test
  public void testTask() {
    assertEquals("test", taskMeta.getName());
  }
}
