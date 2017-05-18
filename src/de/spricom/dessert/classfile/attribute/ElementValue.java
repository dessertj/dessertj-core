package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import de.spricom.dessert.classfile.FieldType;
import de.spricom.dessert.classfile.constpool.ConstantPoolEntry;
import de.spricom.dessert.classfile.constpool.ConstantUtf8;

public class ElementValue {
	private final char tag;
	private ConstantPoolEntry constantValue;
	private FieldType type;
	private Annotation annotation;
	private ElementValue[] values;

	public ElementValue(DataInputStream is, ConstantPoolEntry[] constantPoolEntries) throws IOException {
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
			constantValue = constantPoolEntries[is.readUnsignedShort()];
			break;
		case 'e':
			type = new FieldType(((ConstantUtf8) constantPoolEntries[is.readUnsignedShort()]).getValue());
			constantValue = constantPoolEntries[is.readUnsignedShort()];
			break;
		case 'c':
			type = new FieldType(((ConstantUtf8) constantPoolEntries[is.readUnsignedShort()]).getValue());
			break;
		case '@':
			annotation = new Annotation(is, constantPoolEntries);
			break;
		case '[':
			values = new ElementValue[is.readUnsignedShort()];
			for (int i = 0; i < values.length; i++) {
				values[i] = new ElementValue(is, constantPoolEntries);
			}
			break;
		default:
			throw new IllegalArgumentException("Invalid ElementValue tag: " + tag);
		}
	}

	public void addDependendClassNames(Set<String> classNames) {
		if (type != null) {
			type.addDependendClassNames(classNames);
		}
		if (annotation != null) {
			annotation.addDependendClassNames(classNames);
		}
		if (values != null) {
			for (ElementValue value : values) {
				value.addDependendClassNames(classNames);
			}
		}
	}

	public char getTag() {
		return tag;
	}

	public ConstantPoolEntry getConstantValue() {
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
