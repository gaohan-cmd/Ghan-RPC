package com.ghan.rpc.model;

import java.lang.reflect.Method;

public class Example {
    private Class<?>[] parameterTypes;

    public Example(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void printParameterTypes() {
        for (Class<?> type : parameterTypes) {
            System.out.println(type.getName());
        }
    }

    public static void main(String[] args) throws Exception {
        Method method = Example.class.getMethod("exampleMethod", String.class, int.class);
        Class<?>[] types = method.getParameterTypes();
        
        Example example = new Example(types);
        example.printParameterTypes();
    }
    
    public void exampleMethod(String s, int i) {
        // method implementation
    }
}
