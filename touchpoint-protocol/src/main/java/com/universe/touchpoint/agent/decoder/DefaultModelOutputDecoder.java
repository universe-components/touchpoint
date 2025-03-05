package com.universe.touchpoint.agent.decoder;

import com.universe.touchpoint.agent.AIModelOutputDecoder;
import com.universe.touchpoint.utils.ClassUtils;

import java.lang.reflect.Field;

public class DefaultModelOutputDecoder<C> implements AIModelOutputDecoder<C, C> {

    @Override
    public C run(String params, Class<C> clazz) {
        try {
            C touchPointInstance = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();

            // 遍历字段并填充值
            String[] paramsArr = params.split(", ");
            for (int i = 0; i < fields.length && i < paramsArr.length; i++) {
                Field field = fields[i];
                field.setAccessible(true); // 确保可以访问私有字段
                Object value = ClassUtils.convertToFieldType(field.getType(), paramsArr[i]); // 转换为字段的类型
                field.set(touchPointInstance, value); // 设置字段值
            }

            // 返回填充后的对象
            return touchPointInstance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create or populate inputClass instance", e);
        }
    }

}
