package com.universe.touchpoint.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Pair;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

public class ApkUtils {

    public static List<Pair<String, List<Object>>> getClassNames(
            Context context,
            Class<? extends Annotation> annotationClass,
            List<String> propertyNames,
            boolean canLoad) {
        List<Pair<String, List<Object>>> result = new ArrayList<>();
        try {
            // 获取应用的 APK 文件路径
            String apkPath = context.getApplicationInfo().sourceDir;
            if (!canLoad) {
                File dexFile = new File(apkPath);
                dexFile.setWritable(false);
            }

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
                        List<Object> annotationValues = new ArrayList<>();
                        // 遍历属性名列表，获取每个属性值
                        for (String propertyName : propertyNames) {
                            try {
                                // 调用方法获取属性值
                                Object value = AnnotationUtils.getAnnotationValue(annotation, annotationClass, propertyName);
                                assert value != null;
                                annotationValues.add(value);
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

    public static String getApkPath(Context context) {
        try {
            // 获取应用程序的包信息
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            // 返回APK文件路径
            return appInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
