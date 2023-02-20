package de.spricom.dessert.classfile.constpool;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
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

import java.util.BitSet;

class ConstantMethodHandle extends ConstantPoolEntry {
    public static final int TAG = 15;

    private final ReferenceKind referenceKind;
    private final int referenceIndex;

    public ConstantMethodHandle(int referenceKind, int referenceIndex) {
        this.referenceKind = ReferenceKind.values()[referenceKind];
        this.referenceIndex = referenceIndex;
    }

    @Override
    void recordReferences(BitSet references) {
        references.set(referenceIndex);
    }

    @Override
    public String dump() {
        return dump(referenceKind.ordinal() + ":" + referenceIndex,
                referenceKind.name() + "(" + referenceKind.interpretation + "): "
                        + getConstantPoolEntry(referenceIndex).dump());
    }

    public ReferenceKind getReferenceKind() {
        return referenceKind;
    }

    <T extends ConstantPoolEntry> T getReference() {
        return getConstantPoolEntry(referenceIndex);
    }
}
