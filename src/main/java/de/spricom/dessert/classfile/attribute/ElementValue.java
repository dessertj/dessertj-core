package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import de.spricom.dessert.classfile.constpool.ConstantPool;
import de.spricom.dessert.classfile.constpool.ConstantValue;
import de.spricom.dessert.classfile.constpool.FieldType;
import de.spricom.dessert.classfile.dependency.DependencyHolder;

public class ElementValue implements DependencyHolder {
	private final char tag;
	private ConstantValue<?> constantValue;
	private FieldType type;
	private Annotation annotation;
	private ElementValue[] values;

	public ElementValue(DataInputStream is, ConstantPool constantPool) throws IOException {
		tag = (char) is.readUnsignedByte();
		switch (tag) {
		case 'B':
		case 'C':
		case 'D':
		case 'F':
		case 'I':
		case 'J':
		case 'S':
		case 'Z':
		case 's':
			constantValue = constantPool.getConstantValue(is.readUnsignedShort());
			break;
		case 'e':
			type = constantPool.getFieldType(is.readUnsignedShort());
			constantValue = constantPool.getConstantValue(is.readUnsignedShort());
			break;
		case 'c':
			type = constantPool.getFieldType(is.readUnsignedShort());
			break;
		case '@':
			annotation = new Annotation(is, constantPool);
			break;
		case '[':
			values = new ElementValue[is.readUnsignedShort()];
			for (int i = 0; i < values.length; i++) {
				values[i] = new ElementValue(is, constantPool);
			}
			break;
		default:
			throw new IllegalArgumentException("Invalid ElementValue tag: " + tag);
		}
	}

	public void addDependentClassNames(Set<String> classNames) {
		if (type != null) {
			type.addDependentClassNames(classNames);
		}
		if (annotation != null) {
			annotation.addDependentClassNames(classNames);
		}
		if (values != null) {
			for (ElementValue value : values) {
				value.addDependentClassNames(classNames);
			}
		}
	}

	public char getTag() {
		return tag;
	}

	public ConstantValue<?> getConstantValue() {
		return constantValue;
	}

	public FieldType getType() {
		return type;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public ElementValue[] getValues() {
		return values;
	}
}
