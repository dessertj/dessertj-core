package de.spricom.dessert.classfile.constpool;

import java.util.Set;

import de.spricom.dessert.classfile.dependency.DependencyHolder;

public class FieldType  implements DependencyHolder {
	private int arrayDimensions;
	private Class<?> primitiveType;
	private String objectTypeClassname;

	public FieldType(String descriptor) {
		int i = 0;
		while (descriptor.charAt(i) == '[') {
			arrayDimensions++;
			i++;
		}
		switch (descriptor.charAt(i)) {
		case 'B':
			primitiveType = Byte.TYPE;
			break;
		case 'C':
			primitiveType = Character.TYPE;
			break;
		case 'D':
			primitiveType = Double.TYPE;
			break;
		case 'F':
			primitiveType = Float.TYPE;
			break;
		case 'I':
			primitiveType = Integer.TYPE;
			break;
		case 'J':
			primitiveType = Long.TYPE;
			break;
		case 'L':
			objectTypeClassname = descriptor.substring(i + 1, descriptor.indexOf(';', i + 1)).replace('/', '.');
			break;
		case 'S':
			primitiveType = Short.TYPE;
			break;
		case 'V':
			primitiveType = Void.TYPE;
			break;
		case 'Z':
			primitiveType = Boolean.TYPE;
			break;
		default:
			throw new IllegalArgumentException("Invalid field descriptor: " + descriptor);
		}
	}

	public int getDescriptorLength() {
		if (isPrimitiveType()) {
			return arrayDimensions + 1;
		} else {
			return arrayDimensions + objectTypeClassname.length() + 2;
		}
	}

	public boolean isArrayType() {
		return arrayDimensions > 0;
	}

	public boolean isPrimitiveType() {
		return primitiveType != null && !isVoidType();
	}

	public boolean isVoidType() {
		return Void.TYPE == primitiveType;
	}

	public boolean isObjectType() {
		return primitiveType == null;
	}

	public String getDeclaration() {
		StringBuilder sb = new StringBuilder();
		if (primitiveType != null) {
			sb.append(primitiveType.getName());
		} else {
			sb.append(objectTypeClassname);
		}
		for (int i = 0; i < arrayDimensions; i++) {
			sb.append("[]");
		}
		return sb.toString();
	}
	
	public final void addDependentClassNames(Set<String> classNames) {
		if (isObjectType()) {
			classNames.add(getObjectTypeClassname());
		}
	}

	public String toString() {
		return getDeclaration();
	}

	public String getObjectTypeClassname() {
		return objectTypeClassname;
	}

	public void setObjectTypeClassname(String objectTypeClassname) {
		this.objectTypeClassname = objectTypeClassname;
	}

	public int getArrayDimensions() {
		return arrayDimensions;
	}

	public Class<?> getPrimitiveType() {
		return primitiveType;
	}
}
