package de.spricom.dessert.classfile.attribute;

import de.spricom.dessert.classfile.constpool.ConstantPool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

public class RuntimeVisibleParameterAnnotationsAttribute extends AttributeInfo {
    private final ParameterAnnotation[] parameterAnnotations;

    public RuntimeVisibleParameterAnnotationsAttribute(String name, DataInputStream is, ConstantPool constantPool)
            throws IOException {
        super(name);
        is.readInt(); // skip length
        parameterAnnotations = new ParameterAnnotation[is.readUnsignedByte()];
        for (int i = 0; i < parameterAnnotations.length; i++) {
            parameterAnnotations[i] = new ParameterAnnotation(is, constantPool);
        }
    }

    public ParameterAnnotation[] getParameterAnnotations() {
        return parameterAnnotations;
    }

    public void addDependentClassNames(Set<String> classNames) {
        for (ParameterAnnotation parameterAnnotation : parameterAnnotations) {
            parameterAnnotation.addDependentClassNames(classNames);
        }
    }
}
