package de.spricom.dessert.classfile;

import java.util.Set;

import de.spricom.dessert.classfile.attribute.AttributeInfo;
import de.spricom.dessert.classfile.dependency.DependencyHolder;

abstract class MemberInfo implements DependencyHolder {
	private int accessFlags;
	private String name;
	private String descriptor;
	private AttributeInfo[] attributes;

	protected boolean is(int accessFlag) {
		return (accessFlags & accessFlag) == accessFlag;
	}

	public void addDependentClassNames(Set<String> classNames) {
		for (AttributeInfo attribute : attributes) {
			attribute.addDependentClassNames(classNames);
		}
	}

	public abstract String getDeclaration();

	public String toString() {
		return getDeclaration();
	}

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
