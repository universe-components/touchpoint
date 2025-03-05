package com.universe.touchpoint.spring;

import com.universe.touchpoint.meta.data.AgentMeta;
import com.universe.touchpoint.annotations.task.TouchPointAgent;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.meta.annotation.AgentAnnotationMeta;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.Map;
import javax.annotation.Nonnull;

public class AgentRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(@Nonnull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(@Nonnull AnnotationMetadata importingClassMetadata, @Nonnull BeanDefinitionRegistry registry) {
        Class<?> agentClass = ClassUtils.resolveClassName(importingClassMetadata.getClassName(), null);
        if (importingClassMetadata.hasAnnotation(TouchPointAgent.class.getName())) {
            String agentClassName = TouchPointAgent.class.getName();
            Map<String, Object> agentAttributes = importingClassMetadata.getAnnotationAttributes(agentClassName);
            assert agentAttributes != null;
            String agentName = (String) agentAttributes.get("name");
            AgentAnnotationMeta agentAnnotationMeta = new AgentAnnotationMeta(agentClass, agentAttributes);
            try {
                assert agentName != null;
                AgentMeta agentMeta = new AgentMeta(
                        agentName,
                        environment.getProperty("spring.application.name", "default"),
                        agentClassName,
                        agentAnnotationMeta.getDescription(),
                        agentAnnotationMeta.getLangModel(),
                        agentAnnotationMeta.getVisionModel(),
                        agentAnnotationMeta.getVisionLangModel(),
                        agentAnnotationMeta.getTransportConfig(),
                        agentAnnotationMeta.getMetricSocketConfig(),
                        agentAnnotationMeta.getAgentSocketConfig(),
                        agentAnnotationMeta.getTaskMetricConfig(),
                        agentAnnotationMeta.getActionMetricConfig()
                );
                ((MetaRegion) TouchPointMemory.getRegion(Region.META)).putTouchPointAgent(agentName, agentMeta);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
