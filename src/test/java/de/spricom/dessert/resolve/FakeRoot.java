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

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FakeRoot extends ClassRoot {
    private Map<String, ClassPackage> packages;
    private ClassCollector collector;

    public FakeRoot(File file) {
        super(file);
    }

    @Override
    protected void scan(ClassCollector collector) {
        this.collector = collector;
        packages = new HashMap<String, ClassPackage>();
        collector.addPackage(this);
        packages.put("", this);
    }

    @Override
    public URL getResource(String name) {
        return null;
    }

    public void add(String classname) {
        ClassPackage pckg = ensurePackage(parentPackageName(classname));
        FakeClassEntry ce = new FakeClassEntry(classname, pckg);
        pckg.addClass(ce);
        collector.addClass(ce);
    }

    private ClassPackage ensurePackage(String packageName) {
        ClassPackage pckg = packages.get(packageName);
        if (pckg != null) {
            return pckg;
        }
        ClassPackage parent = ensurePackage(parentPackageName(packageName));
        pckg = new ClassPackage(parent, packageName);
        collector.addPackage(pckg);
        packages.put(packageName, pckg);
        return pckg;
    }

    private String parentPackageName(String packageName) {
        int index = packageName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        String parentPackageName = packageName.substring(0, index);
        return parentPackageName;
    }
}
