package de.spricom.dessert.classfile.attribute;

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

import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.classfile.dependency.DependencyHolder;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

public class ParameterAnnotation implements DependencyHolder {
    private final Annotation[] annotations;

    public ParameterAnnotation(DataInputStream is, ConstantPool constantPool) throws IOException {
        annotations = new Annotation[is.readUnsignedShort()];
        for (int i = 0; i < annotations.length; i++) {
            annotations[i] = new Annotation(is, constantPool);
        }
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    @Override
    public void addDependentClassNames(Set<String> classNames) {
        for (Annotation annotation : annotations) {
            annotation.addDependentClassNames(classNames);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Annotation annotation : annotations) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(annotation);
        }
        return sb.toString();
    }
}
