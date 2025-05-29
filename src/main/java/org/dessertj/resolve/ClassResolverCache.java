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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ClassResolverCache implements ClassCollector {
    private final Map<String, ClassPackage> packages = new HashMap<String, ClassPackage>(3000);
    private final Map<String, ClassEntry> classes = new HashMap<String, ClassEntry>(60000);
    private final Map<String, List<ClassEntry>> duplicates = new HashMap<String, List<ClassEntry>>();

    @Override
    public void addPackage(ClassPackage pckg) {
        String pn = pckg.getPackageName();
        ClassPackage prev = packages.get(pn);
        if (prev == null) {
            packages.put(pn, pckg);
        } else {
            prev.addAlternative(pckg);
        }
    }

    @Override
    public void addClass(ClassEntry ce) {
        String cn = ce.getClassname();
        ClassEntry prev = classes.get(cn);
        if (prev == null) {
            classes.put(cn, ce);
        } else {
            prev.addAlternative(ce);
            addDuplicates(cn, prev.getAlternatives());
        }
    }

    private void addDuplicates(String cn, List<ClassEntry> alternatives) {
        if (duplicates.containsKey(cn)) {
            return;
        }
        ClassPackage pckg = alternatives.get(0).getPackage();
        for (ClassEntry alternative : alternatives) {
            if (pckg != alternative.getPackage()) {
                duplicates.put(cn, alternatives);
                return;
            }
        }
    }

    ClassPackage getPackage(String packageName) {
        return packages.get(packageName);
    }

    ClassEntry getClassEntry(String classname) {
        return classes.get(classname);
    }

    Map<String, List<ClassEntry>> getDuplicates() {
        return duplicates;
    }

    int getPackageCount() {
        return packages.size();
    }

    int getClassCount() {
        return classes.size();
    }
}
