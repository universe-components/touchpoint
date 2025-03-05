package com.universe.touchpoint.android.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.util.Pair;

import com.universe.touchpoint.utils.AnnotationUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import dalvik.system.DexFile;

public class ApkUtils {

    public static Map<String, String> loadProperties(Context context) {
        Map<String, String> propertiesMap = new HashMap<>();
        Properties properties = new Properties();

        try {
            // 获取 AssetManager
            AssetManager assetManager = context.getAssets();

            // 打开 properties 文件
            InputStream inputStream = assetManager.open("local.properties");

            // 加载 properties 文件
            properties.load(inputStream);

            // 将 properties 文件内容转换为 Map
            for (String key : properties.stringPropertyNames()) {
                propertiesMap.put(key, properties.getProperty(key));
            }

            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return propertiesMap;
    }

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

    public static Map<String, Map<String, Map<String, Object>>> getFieldAnnotationValues(
            Context context,
            Class<? extends Annotation> annotationClass,
            List<Class<? extends Annotation>> extractAnnotationClasses,
            String aggregateProperty,
            boolean canLoad) {
        Map<String, Map<String, Map<String, Object>>> result = new HashMap<>();
        try {
            String apkPath = context.getApplicationInfo().sourceDir;
            if (!canLoad) {
                File dexFile = new File(apkPath);
                dexFile.setWritable(false);
            }

            DexFile dexFile = new DexFile(apkPath);
            Enumeration<String> classNamesEnum = dexFile.entries();

            while (classNamesEnum.hasMoreElements()) {
                String className = classNamesEnum.nextElement();
                try {
                    Class<?> loadedClass = context.getClassLoader().loadClass(className);
                    Field[] fields = loadedClass.getDeclaredFields();

                    for (Field field : fields) {
                        Map<String, Map<String, Object>> annotationMap = new HashMap<>();
                        if (field.isAnnotationPresent(annotationClass)) {
                            Object aggregatePropertyValue = AnnotationUtils.getAnnotationValue(field.getAnnotation(annotationClass), annotationClass, aggregateProperty);
                            for (Class<? extends Annotation> extractAnnotationClass : extractAnnotationClasses) {
                                Annotation annotation = field.getAnnotation(extractAnnotationClass);
                                Map<String, Object> annotationValues = new HashMap<>();

                                // 获取注解中的所有方法（属性）
                                Method[] methods = extractAnnotationClass.getDeclaredMethods();
                                for (Method method : methods) {
                                    try {
                                        // 调用每个方法获取属性值
                                        Object value = method.invoke(annotation);
                                        if (value != null) {
                                            annotationValues.put(method.getName(), value);
                                        }
                                    } catch (Exception e) {
                                        annotationValues.put(method.getName(), null);
                                    }
                                }

                                if (!annotationValues.isEmpty()) {
                                    annotationMap.put(extractAnnotationClass.getSimpleName(), annotationValues);
                                }
                            }
                            result.put((String) aggregatePropertyValue, annotationMap);
                        }
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
