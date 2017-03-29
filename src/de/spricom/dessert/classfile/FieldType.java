package de.spricom.dessert.classfile;

public class FieldType {
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
			objectTypeClassname = descriptor.substring(i + 1, descriptor.indexOf(';', i+1)).replace('/', '.');
			break;
		case 'S':
			primitiveType = Short.TYPE;
			break;
		case 'Z':
			primitiveType = Boolean.TYPE;
			break;
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
		return primitiveType != null;
	}
	
	public boolean isObjectType() {
		return primitiveType == null;
	}
	
	public String getName() {
		StringBuilder sb = new StringBuilder();
		if (isPrimitiveType()) {
			sb.append(primitiveType.getName());
		} else {
			sb.append(objectTypeClassname);
		}
		for (int i = 0; i < arrayDimensions; i++) {
			sb.append("[]");
		}
		return sb.toString();
	}
	
	public String toString() {
		return getName();
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
