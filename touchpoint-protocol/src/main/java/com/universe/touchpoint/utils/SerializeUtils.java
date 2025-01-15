package com.universe.touchpoint.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SerializeUtils {

    public static byte[] serializeToByteArray(Object object) {
        Kryo kryo = new Kryo();
        // 注册类，这样 Kryo 可以处理该类的序列化和反序列化
        kryo.register(object.getClass());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeObject(output, object);
        output.close();

        return byteArrayOutputStream.toByteArray();
    }

    public static <T> T deserializeFromByteArray(byte[] data, Class<T> clazz) {
        Kryo kryo = new Kryo();
        kryo.register(clazz);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Input input = new Input(byteArrayInputStream);
        T object = kryo.readObject(input, clazz);
        input.close();

        return object;
    }

}
