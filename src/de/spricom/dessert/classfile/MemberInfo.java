package de.spricom.dessert.classfile;

abstract class MemberInfo {
	protected int accessFlags;
	private String name;
	private String descriptor;
	private AttributeInfo[] attributes;

	public int getAccessFlags() {
		return accessFlags;
	}

	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public AttributeInfo[] getAttributes() {
		return attributes;
	}

	public void setAttributes(AttributeInfo[] attributes) {
		this.attributes = attributes;
	}
}
