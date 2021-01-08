package de.spricom.dessert.classfile.constpool;

import java.util.BitSet;
import java.util.Set;

class ConstantInvokeDynamic extends ConstantDynamic {
	public static final int TAG = 18;

	public ConstantInvokeDynamic(int bootstrapMethodAttrIndex, int referenceIndex) {
		super(bootstrapMethodAttrIndex, referenceIndex);
	}
}
