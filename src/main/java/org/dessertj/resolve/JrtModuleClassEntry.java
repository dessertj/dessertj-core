package org.dessertj.resolve;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2025 Hans Jörg Heßmann
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.dessertj.classfile.ClassFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

final class JrtModuleClassEntry extends ClassEntry {
    private static final Logger log = Logger.getLogger(JrtModuleClassEntry.class.getName());

    private final URI uri;

    JrtModuleClassEntry(ClassPackage pckg, String simpleName, URI uri) {
        super(pckg.getClassName(simpleName), pckg);
        this.uri = uri;
    }

    @Override
    public ClassFile resolveClassFile() {
        InputStream is = null;
        try {
            URL url = uri.toURL();
            is = url.openStream();
            ClassFile cf = new ClassFile(is);
            return cf;
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read " + uri, ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    log.log(Level.SEVERE, "Cannot close stream after reading " + uri, ex);
                }
            }
        }
    }

    @Override
    public URI getURI() {
        return uri;
    }
}
