package com.universe.touchpoint.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationUtils {

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
