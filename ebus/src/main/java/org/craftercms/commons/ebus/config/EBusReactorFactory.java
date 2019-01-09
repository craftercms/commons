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

package org.craftercms.commons.ebus.config;

import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;

/**
 * EBus Reactor Factory.
 *
 * @author Dejan Brkic
 */
public class EBusReactorFactory {

    private Environment env;

    public Reactor createReactor() {
        return Reactors.reactor().env(env).dispatcher(Environment.THREAD_POOL).get();
    }

    public void setEnv(Environment env) {
        this.env = env;
    }
}
