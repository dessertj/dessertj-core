package org.dessertj.tools;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2023 Hans Jörg Heßmann
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

import org.dessertj.slicing.Classpath;
import org.dessertj.slicing.Root;
import org.junit.Test;

public class DumpModuleTool {

    private static final String[] EXCLUSIONS = {
            "classfile.constpool",
            "classfile.dependency",
            "matching",
            "modules.java",
            "modules.jdk"
    };

    @Test
    public void go() {
        Classpath cp = new Classpath();
        Root dessertj = cp.rootOf(Classpath.class);
        System.out.println("module org.dessertj.core {");
        System.out.println("    requires java.logging;");
        System.out.println();
        for (String packageName : dessertj.partitionByPackage().keySet()) {
            if (!isExcluded(packageName) && !packageName.isEmpty()) {
                System.out.println("    exports " + packageName + ";");
            }
        }
        System.out.println("}");
    }

    private boolean isExcluded(String packageName) {
        for (String exclusion : EXCLUSIONS) {
            if (packageName.endsWith(exclusion)) {
                return true;
            }
        }
        return false;
    }
}
