package com.universe.touchpoint.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Pair;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

public class ApkUtils {

    public static List<Pair<String, List<String>>> getClassNames(
            Context context,
            Class<? extends Annotation> annotationClass,
            List<String> propertyNames) {
        List<Pair<String, List<String>>> result = new ArrayList<>();
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
                    // 加载类
                    // 使用 ClassLoader 加载类，避免执行静态初始化代码
                    Class<?> loadedClass = context.getClassLoader().loadClass(className);

                    // 检查类是否包含目标注解
                    if (loadedClass.isAnnotationPresent(annotationClass)) {
                        // 获取注解
                        Annotation annotation = loadedClass.getAnnotation(annotationClass);
                        // 存储属性值的列表
                        List<String> annotationValues = new ArrayList<>();
                        // 遍历属性名列表，获取每个属性值
                        for (String propertyName : propertyNames) {
                            try {
                                // 调用方法获取属性值
                                Object value = AnnotationUtils.getAnnotationValue(annotation, annotationClass, propertyName);
                                assert value != null;
                                annotationValues.add(value.toString());
                            } catch (Exception e) {
                                // 如果属性获取失败，记录异常信息或忽略
                                annotationValues.add(null);
                            }
                        }
                        // 将类名和注解属性值列表加入结果
                        result.add(new Pair<>(className, annotationValues));
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
