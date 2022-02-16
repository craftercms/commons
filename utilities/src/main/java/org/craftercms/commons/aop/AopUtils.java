/*
 * Copyright (C) 2007-2022 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
                throw new IllegalStateException("Couldn't find implementation of method in target class: " + method.toGenericString(), e);
            }
        }

        return method;
    }

}
