package de.spricom.dessert.classfile.constpool;

import java.util.BitSet;
import java.util.Set;

class ConstantMethodType extends ConstantPoolEntry {
    public static final int TAG = 16;
    private final int descriptorIndex;
    private MethodType type;

    public ConstantMethodType(int referenceIndex) {
        this.descriptorIndex = referenceIndex;
    }

    @Override
    void recordReferences(BitSet references) {
        references.set(descriptorIndex);
    }

    @Override
    public String dump() {
        return dump(index(descriptorIndex), getMethodType().toString());
    }

    public MethodType getMethodType() {
        if (type == null) {
            ConstantUtf8 descriptor = getConstantPoolEntry(descriptorIndex);
            type = new MethodType(descriptor.getValue());
        }
        return type;
    }

    @Override
    public void addDependentClassNames(Set<String> classNames) {
        getMethodType().addDependentClassNames(classNames);
    }
}
