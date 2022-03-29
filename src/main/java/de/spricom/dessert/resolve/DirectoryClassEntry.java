package de.spricom.dessert.resolve;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2022 Hans Jörg Heßmann
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

import de.spricom.dessert.classfile.ClassFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

final class DirectoryClassEntry extends ClassEntry {
    private static final Logger log = Logger.getLogger(DirectoryClassEntry.class.getName());

    private final File classFile;

    DirectoryClassEntry(ClassPackage pckg, File classFile) {
        super(pckg.getClassName(simpleName(classFile)), pckg);
        this.classFile = classFile;
    }

    private static String simpleName(File classFile) {
        return classFile.getName().substring(0, classFile.getName().length() - ".class".length());
    }

    @Override
    public ClassFile resolveClassFile() {
        InputStream is = null;
        try {
            is = new FileInputStream(classFile);
            ClassFile cf = new ClassFile(is);
            return cf;
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read " + classFile.getAbsolutePath(), ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    log.log(Level.SEVERE, "Cannot close stream after reading " + classFile.getAbsolutePath(), ex);
                }
            }
        }
    }

    @Override
    public URI getURI() {
        return classFile.toURI();
    }
}
