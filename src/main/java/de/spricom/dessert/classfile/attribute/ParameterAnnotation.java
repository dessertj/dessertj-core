package de.spricom.dessert.classfile.attribute;

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

    @Override
    public void addDependentClassNames(Set<String> classNames) {
        for (Annotation annotation : annotations) {
            annotation.addDependentClassNames(classNames);
        }
    }
}
