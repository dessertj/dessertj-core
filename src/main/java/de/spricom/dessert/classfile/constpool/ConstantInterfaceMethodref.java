package de.spricom.dessert.classfile.constpool;

import java.util.BitSet;
import java.util.Set;

class ConstantInterfaceMethodref extends ConstantMethodref {
	public static final int TAG = 11;

	public ConstantInterfaceMethodref(int classIndex, int nameAndTypeIndex) {
		super(classIndex, nameAndTypeIndex);
	}
}
