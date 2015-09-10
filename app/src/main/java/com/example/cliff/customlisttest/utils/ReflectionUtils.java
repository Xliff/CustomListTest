package com.example.cliff.customlisttest.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Cliff on 9/9/2015.
 */
public class ReflectionUtils {

    public static void invokeMethod(String methodName, Object o) {
        // cw: Obviously if you use these routines, you should know what you are doing.
        // I plan on using this for ONLY ONE class, however you never know in development when
        // you might need a tool. I wrote this so that I'd have it when I needed it. It's cleaner
        // than checking instanceof in increasingly tall if- or case- towers.

        Method method = null;
        try {
            method = o.getClass().getMethod(methodName);

            // Call method.
            try {
                method.invoke(o);
            } catch (IllegalArgumentException e) {
                // Die quietly.
            } catch (IllegalAccessException e) {
                // Die quieter.
            } catch (InvocationTargetException e) {
                // Die quietest
            }

        } catch (SecurityException e) {
            // Die quietly, still.
        } catch (NoSuchMethodException e) {
            // Still... die, quietly.
        }

    }

}
