package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import de.spricom.dessert.classfile.constpool.ConstantPool;

public class RuntimeVisibleAnnotationsAttribute extends AttributeInfo {
	private final Annotation[] annotations;

	public RuntimeVisibleAnnotationsAttribute(String name, DataInputStream is, ConstantPool constantPool)
			throws IOException {
		super(name);
		is.readInt(); // skip length
		annotations = new Annotation[is.readUnsignedShort()];
		for (int i = 0; i < annotations.length; i++) {
			annotations[i] = new Annotation(is, constantPool);
		}
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}

	public void addDependentClassNames(Set<String> classNames) {
		for (Annotation annotation : annotations) {
			annotation.addDependentClassNames(classNames);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		boolean first = true;
		for (Annotation annotation : annotations) {
			if (first) {
				sb.append(": ");
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(annotation);
		}
		return sb.toString();
	}
}
