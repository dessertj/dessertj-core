package de.spricom.dessert.classfile.constpool;

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
