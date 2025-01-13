package com.universe.touchpoint.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Pair;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

public class ApkUtils {

    public static List<Pair<String, String>> getClassNames(
            Context context,
            Class<? extends Annotation> annotationClass,
            String propertyName) {
        List<Pair<String, String>> result = new ArrayList<>();
        try {
            // 获取应用的 APK 文件路径
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), 0);
            String apkPath = appInfo.sourceDir;

            // 使用 DexFile 获取 APK 中的所有类
            DexFile dexFile = new DexFile(apkPath);
            Enumeration<String> classNamesEnum = dexFile.entries();

            // 遍历所有类名
            while (classNamesEnum.hasMoreElements()) {
                String className = classNamesEnum.nextElement();
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (className.startsWith("ai.djl")) {
                            continue;
                        }
                    }
                    // 加载类
                    // 使用 ClassLoader 加载类，避免执行静态初始化代码

                    Class<?> loadedClass = context.getClassLoader().loadClass(className);

                    // 检查类是否包含目标注解
                    if (loadedClass.isAnnotationPresent(annotationClass)) {
                        // 获取注解
                        Annotation annotation = loadedClass.getAnnotation(annotationClass);
                        // 获取注解中的 value 属性
                        String annotationValue = AnnotationUtils.getAnnotationValue(annotation, annotationClass, propertyName);
                        // 将类名和注解值加入结果
                        result.add(new Pair<>(className, annotationValue));
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
