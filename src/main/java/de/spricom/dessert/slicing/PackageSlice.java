package de.spricom.dessert.slicing;

/*-
 * #%L
 * Dessert Dependency Assertion Library
 * %%
 * Copyright (C) 2017 - 2021 Hans Jörg Heßmann
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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * A slice represents (subset of) a single Java package for one concrete root.
 * It's elements are .class files contained in the package. A root is either a
 * directory in the file-system or a jar file. Hence each elements of a slice is
 * unique. (There may be two classes with the same name on the classpath, but
 * with the combination of classname and root given by a PackageSlice the class is
 * uniquely defined.) A slice may represent a subset of the .class files in a
 * package, for example all interfaces, all classes complying some naming
 * convention, all classes implementing some interfaces, all inner classes etc.
 */
public class PackageSlice extends PartitionSlice {
    private final Map<String, PackageSlice> otherPackages;

    private PackageSlice(String packageName, Set<Clazz> entries, Map<String, PackageSlice> otherPackages) {
        super(packageName, entries);
        this.otherPackages = otherPackages;
    }

    public static SlicePartitioner partitioner() {
        return new SlicePartitioner() {
            @Override
            public String partKey(Clazz clazz) {
                return clazz.getPackageName();
            }
        };
    }

    public static PartitionSliceFactory<PackageSlice> factory() {
        return new PartitionSliceFactory<PackageSlice>() {
            @Override
            public PackageSlice createPartSlice(String packageName, Set<Clazz> entries, Map<String, PackageSlice> slices) {
                return new PackageSlice(packageName, entries, slices);
            }
        };
    }

    public String getPackageName() {
        return getPartKey();
    }

    public String getParentPackageName() {
        String packageName = getPackageName();
        int pos = packageName.lastIndexOf('.');
        if (pos == -1) {
            return "";
        }
        return packageName.substring(0, pos);
    }

    public PackageSlice getParentPackage() {
        PackageSlice parentPackage = otherPackages.get(getParentPackageName());
        if (parentPackage == null) {
            parentPackage = new PackageSlice(getParentPackageName(), Collections.<Clazz>emptySet(), otherPackages);
        }
        return parentPackage;
    }
}
