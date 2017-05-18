package de.spricom.dessert.classfile.attribute;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import de.spricom.dessert.classfile.constpool.ConstantPoolEntry;
import de.spricom.dessert.classfile.constpool.ConstantUtf8;

public class RuntimeVisibleAnnotationsAttribute extends AttributeInfo {
	private final Annotation[] annotations;

	public RuntimeVisibleAnnotationsAttribute(ConstantUtf8 name, DataInputStream is,
			ConstantPoolEntry[] constantPoolEntries) throws IOException {
		super(name.getValue());
		is.readInt(); // skip length
		annotations = new Annotation[is.readUnsignedShort()];
		for (int i = 0; i < annotations.length; i++) {
			annotations[i] = new Annotation(is, constantPoolEntries);
		}
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}
	
	public void addDependendClassNames(Set<String> classNames) {
		for (Annotation annotation : annotations) {
			annotation.addDependendClassNames(classNames);
		}
	}
}
