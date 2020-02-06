/*
 * Copyright (C) 2007-2020 Crafter Software Corporation. All Rights Reserved.
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

package org.craftercms.commons.upgrade.impl.providers;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * Implementation of {@link org.craftercms.commons.upgrade.VersionProvider} that handles YAML files
 *
 * @author joseross
 * @since 3.1.5
 */
public class YamlFileVersionProvider extends AbstractFileVersionProvider {

    protected final Yaml yaml;

    public YamlFileVersionProvider() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        yaml = new Yaml(options);
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> loadFile(Path file) throws IOException {
        try (InputStream is = Files.newInputStream(file)) {
            return yaml.loadAs(is, Map.class);
        }
    }

    @Override
    protected String readVersionFromFile(final Path file) throws Exception {
        Map<String, Object> values = loadFile(file);
        return (String)values.get(VERSION);
    }

    @Override
    protected void writeVersionToFile(final Path file, final String version) throws Exception {
        Map<String, Object> values = loadFile(file);
        values.put(VERSION, version);
        try (Writer writer = Files.newBufferedWriter(file)) {
            yaml.dump(values, writer);
        }
    }
}
