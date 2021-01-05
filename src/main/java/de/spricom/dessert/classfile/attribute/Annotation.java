package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.classfile.constpool.FieldType;
import de.spricom.dessert.classfile.dependency.DependencyHolder;

public class Annotation implements DependencyHolder {
	private FieldType type;
	private ElementValuePair[] elementValuePairs;

	public Annotation(DataInputStream is, ConstantPool constantPool) throws IOException {
		type = constantPool.getFieldType(is.readUnsignedShort());
		elementValuePairs = new ElementValuePair[is.readUnsignedShort()];
		for (int i = 0; i < elementValuePairs.length; i++) {
			elementValuePairs[i] = new ElementValuePair(is, constantPool);
		}
	}

	public void addDependentClassNames(Set<String> classNames) {
		type.addDependentClassNames(classNames);
		for (ElementValuePair pair : elementValuePairs) {
			pair.addDependentClassNames(classNames);
		}
	}

	public FieldType getType() {
		return type;
	}

	public ElementValuePair[] getElementValuePairs() {
		return elementValuePairs;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(type.toString());
		for (ElementValuePair pair : elementValuePairs) {
			sb.append(" ").append(pair);
		}
		return sb.toString();
	}
}
