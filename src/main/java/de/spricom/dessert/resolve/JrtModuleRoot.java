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
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class JrtModuleRoot extends ClassRoot {

    private final String moduleName;
    private final ReflectiveJrtFileSystem fs;

    public JrtModuleRoot(String moduleName, ReflectiveJrtFileSystem fs) {
        super(null);
        this.moduleName = moduleName;
        this.fs = fs;
    }

    @Override
    protected void scan(ClassCollector collector) throws IOException {
        try {
            scan(collector, this, fs.getModulePath(moduleName), "");
        } catch (InvocationTargetException ex) {
            if (ex.getTargetException() instanceof IOException) {
                throw (IOException) ex.getTargetException();
            }
            throw new IOException("Unable to read content of " + moduleName + " module.", ex);
        } catch (IllegalAccessException ex) {
            throw new IOException("Cannot access NIO classes.", ex);
        } catch (URISyntaxException ex) {
            throw new IOException("Cannot convert jrt-URI.", ex);
        }
    }

    private void scan(ClassCollector collector, ClassPackage pckg, Object dirPath, String prefix)
            throws InvocationTargetException, IllegalAccessException, URISyntaxException {
        collector.addPackage(pckg);
        for (Object path : fs.newDirectoryStream(dirPath)) {
            String filename = fs.getFileName(path);
            if (fs.isDirectory(path)) {
                String packageName = prefix + filename;
                ClassPackage subPackage = new ClassPackage(pckg, packageName);
                scan(collector, subPackage, path, packageName + ".");
            } else if (filename.endsWith(".class")) {
                String simpleName = filename.substring(0, filename.length() - ".class".length());
                ClassEntry classEntry = new JrtModuleClassEntry(pckg, simpleName, fs.toUri(path));
                pckg.addClass(classEntry);
                collector.addClass(classEntry);
            }
        }
    }

    @Override
    public URL getResource(String name) {
        URI uri = URI.create("jrt:/" + moduleName + "/" + name);
        try {
            URL url = uri.toURL();
            return exists(url) ? url : null;
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid resource name: " + name, ex);
        }
    }

    private boolean exists(URL url) {
        try {
            InputStream in = url.openStream();
            in.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    @Override
    public URI getURI() {
        return URI.create("jrt:/" + moduleName);
    }

}
