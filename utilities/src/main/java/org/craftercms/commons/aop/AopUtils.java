package org.craftercms.commons.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class AopUtils {

    private AopUtils() {
    }


    public static Method getActualMethod(JoinPoint jp) {
        MethodSignature ms = (MethodSignature)jp.getSignature();
        Method method = ms.getMethod();

        if (method.getDeclaringClass().isInterface()) {
            Class<?> targetClass = jp.getTarget().getClass();
            try {
                method = targetClass.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                // Should NEVER happen
                throw new RuntimeException(e);
            }
        }

        return method;
    }

}
