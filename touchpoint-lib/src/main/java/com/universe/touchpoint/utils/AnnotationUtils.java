package com.universe.touchpoint.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationUtils {

    public static String getAnnotationValue(Annotation annotation, Class<? extends Annotation> annotationClass, String propertyName) {
        try {
            Method valueMethod = annotationClass.getMethod(propertyName);
            return (String) valueMethod.invoke(annotation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
