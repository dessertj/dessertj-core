package de.spricom.dessert.classfile.constpool;

import java.util.Set;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.MethodType;

class ConstantMethodType extends ConstantPoolEntry {
	public static final int TAG = 16;
	private final int descriptorIndex;

	public ConstantMethodType(int referenceIndex) {
		this.descriptorIndex = referenceIndex;
	}

	@Override
	public String dump(ClassFile cf) {
		return "methodType: " + cf.getConstantPool()[descriptorIndex].dump(cf);
	}

	public int getDescriptorIndex() {
		return descriptorIndex;
	}
	

	@Override
	protected void addClassNames(Set<String> classNames, ClassFile cf) {
		ConstantUtf8 descriptor = (ConstantUtf8) cf.getConstantPoolEntry(descriptorIndex);
		new MethodType(descriptor.getValue()).addDependendClassNames(classNames);
	}
}
