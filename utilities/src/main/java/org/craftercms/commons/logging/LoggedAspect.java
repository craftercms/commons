/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.commons.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Aspect that uses a {@link org.craftercms.commons.logging.MethodLogger} to log methods with
 * {@link org.craftercms.commons.logging.Logged} annotation (or all methods of a class, if used in a class).
 *
 * @author avasquez
 */
@Aspect
public class LoggedAspect {

    protected MethodLogger methodLogger;

    public LoggedAspect() {
        methodLogger = new I10nMethodLogger();
    }

    public void setMethodLogger(MethodLogger methodLogger) {
        this.methodLogger = methodLogger;
    }

    @Around("@within(org.craftercms.commons.logging.Logged) || @annotation(org.craftercms.commons.logging.Logged)")
    public Object logMethod(ProceedingJoinPoint pjp) throws Throwable {
        String className = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName();
        Object[] args = pjp.getArgs();

        methodLogger.logEntry(className, methodName, args);

        try {
            Object returnValue = pjp.proceed();

            methodLogger.logExit(className, methodName, returnValue);

            return returnValue;
        } catch (Throwable e) {
            methodLogger.logException(className, methodName, e);

            throw e;
        }
    }

}
