package com.universe.touchpoint.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationUtils {

    public static <T> Map<T, Object> annotation2Config(
            Class<?> clazz,
            Map<Class<? extends Annotation>, Class<?>> annotation2Config,
            Map<Class<? extends Annotation>, T> annotation2Type
    ) throws Exception {
        Map<T, Object> configInstances = new HashMap<>();
        annotation2Config(clazz, annotation2Config, configInstances, annotation2Type);

        return configInstances;
    }

    public static <T> void annotation2Config(
            Class<?> clazz,
            Map<Class<? extends Annotation>, Class<?>> annotation2Config,
            Map<T, Object> configInstances,
            Map<Class<? extends Annotation>, T> annotation2Type
    ) throws Exception {
        // 获取类上的所有注解
        for (Annotation annotation : clazz.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();

            // 如果 annotation2Config 中包含该注解类型，则处理
            if (annotation2Config.containsKey(annotationType)) {
                // 获取对应的配置类类型
                Class<?> configClass = annotation2Config.get(annotationType);
                // 获取或创建配置类实例
                Object configInstance = configInstances.computeIfAbsent(
                        annotation2Type.get(annotationType),
                        cls -> {
                            try {
                                assert configClass != null;
                                return configClass.getDeclaredConstructor().newInstance();
                            } catch (Exception e) {
                                throw new RuntimeException("Failed to create instance of " + cls, e);
                            }
                        });
                // 将注解的属性赋值到配置类的成员变量
                for (java.lang.reflect.Method method : annotationType.getDeclaredMethods()) {
                    String propertyName = method.getName();
                    Object value = method.invoke(annotation);
                    try {
                        assert configClass != null;
                        Field field = configClass.getDeclaredField(propertyName);
                        field.setAccessible(true);
                        field.set(configInstance, value);
                    } catch (NoSuchFieldException e) {
                        // 忽略没有对应字段的属性
                    }
                }
            }
        }
    }

    public static Object annotation2Config(
            Class<?> clazz,
            Map<Class<? extends Annotation>, Class<?>> annotation2Config) throws Exception {
        for (Annotation annotation : clazz.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();

            // 如果 annotation2Config 中存在该注解
            if (annotation2Config.containsKey(annotationType)) {
                Class<?> configClass = annotation2Config.get(annotationType);
                // 创建 configClass 实例
                assert configClass != null;
                Object configInstance = configClass.getDeclaredConstructor().newInstance();

                // 获取配置类的所有字段，用于检查是否存在同名变量
                Map<String, Field> configFields = Arrays.stream(configClass.getDeclaredFields())
                        .collect(Collectors.toMap(Field::getName, field -> field));
                for (Field field : configClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    configFields.put(field.getName(), field);
                }

                // 读取注解属性值，并赋值给 configInstance
                for (java.lang.reflect.Method method : annotationType.getDeclaredMethods()) {
                    String propertyName = method.getName();
                    Object value = method.invoke(annotation);

                    // **检查 configClass 是否有同名字段**
                    if (configFields.containsKey(propertyName)) {
                        Field field = configFields.get(propertyName);
                        assert field != null;
                        field.set(configInstance, value);
                    }
                }

                // 直接返回 configInstance
                return configInstance;
            }
        }

        return null; // 如果没有匹配的注解，返回 null
    }

    public static Map<String, Object> getAnnotationValue(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        Map<String, Object> result = new HashMap<>();

        // 获取类上的指定注解
        Annotation annotation = clazz.getAnnotation(annotationClass);
        if (annotation == null) {
            return result; // 返回空Map
        }

        // 反射获取注解的所有方法（即注解的属性）
        for (Method method : annotationClass.getDeclaredMethods()) {
            try {
                // 获取属性值
                Object value = method.invoke(annotation);
                result.put(method.getName(), value);
            } catch (Exception e) {
                throw new RuntimeException("Failed to get annotation value for " + method.getName(), e);
            }
        }
        return result;
    }

    public static Object getAnnotationValue(Annotation annotation, Class<? extends Annotation> annotationClass, String propertyName) {
        try {
            Method valueMethod = annotationClass.getMethod(propertyName);
            return valueMethod.invoke(annotation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
