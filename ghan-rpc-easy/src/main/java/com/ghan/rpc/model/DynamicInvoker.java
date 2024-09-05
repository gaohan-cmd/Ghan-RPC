package com.ghan.rpc.model;

import java.lang.reflect.Method;

public class DynamicInvoker {
    public static void invokeMethod(Object obj, String methodName, Object... args) throws Exception {
        // 获取方法的参数类型
        Class<?>[] parameterTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }

        // 获取方法对象
        Method method = obj.getClass().getMethod(methodName, parameterTypes);

        // 调用方法
        method.invoke(obj, args);
    }

    public static void main(String[] args) throws Exception {
        // 示例对象
        ExampleA example = new ExampleA();

        // 动态调用方法
        invokeMethod(example, "exampleMethod", "Hello", 123);
    }
}

class ExampleA {
    public void exampleMethod(String s, int i) {
        System.out.println("String: " + s + ", int: " + i);
    }
}
