package com.universe.touchpoint.spring;

import com.universe.touchpoint.meta.data.Task;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.meta.annotation.TaskAnnotationMeta;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.socket.context.TaskContext;
import com.universe.touchpoint.spring.utils.BeanUtils;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.Map;
import javax.annotation.Nonnull;

public class AgentTaskRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(@Nonnull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(@Nonnull AnnotationMetadata importingClassMetadata, @Nonnull BeanDefinitionRegistry registry) {
        Class<?> taskClass = ClassUtils.resolveClassName(importingClassMetadata.getClassName(), null);
        if (importingClassMetadata.hasAnnotation(com.universe.touchpoint.annotations.task.Task.class.getName())) {
            String taskClassName = com.universe.touchpoint.annotations.task.Task.class.getName();
            Map<String, Object> taskAttributes = importingClassMetadata.getAnnotationAttributes(taskClassName);
            assert taskAttributes != null;
            String taskName = (String) taskAttributes.get("value");
            TaskAnnotationMeta taskAnnotationMeta = new TaskAnnotationMeta(taskClass, taskAttributes);
            try {
                assert taskName != null;
                BeanUtils.findBeanFactory(registry).registerSingleton(taskName, new Task(
                                    taskName,
                                    environment.getProperty("spring.application.name", "default"),
                                    taskClassName,
                                    taskAnnotationMeta.getDescription(),
                                    taskAnnotationMeta.getLangModel(),
                                    taskAnnotationMeta.getVisionModel(),
                                    taskAnnotationMeta.getVisionLangModel(),
                                    taskAnnotationMeta.getTransportConfig(),
                                    taskAnnotationMeta.getMetricSocketConfig(),
                                    taskAnnotationMeta.getAgentSocketConfig(),
                                    taskAnnotationMeta.getTaskMetricConfig(),
                                    taskAnnotationMeta.getActionMetricConfig()
                            ));
                AgentSocketStateMachine.getInstance(taskName).registerReceiver(new TaskContext(taskName), ActionRole.PROPOSER);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
