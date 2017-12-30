package de.spricom.dessert.classfile.constpool;

import de.spricom.dessert.classfile.dependency.DependencyHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MethodType  implements DependencyHolder {
	private final FieldType[] parameterTypes;
	private final FieldType returnType;
	
	public MethodType(String descriptor) {
		assert descriptor != null : "descriptor == null";
		if ('(' != descriptor.charAt(0)) {
			throw new IllegalArgumentException("Invalid method descriptor: " + descriptor);
		}
		List<FieldType> params = new ArrayList<FieldType>();
		int index = 1;
		while (')' != descriptor.charAt(index)) {
			FieldType param = new FieldType(descriptor.substring(index));
			params.add(param);
			index += param.getDescriptorLength();
		}
		parameterTypes = params.toArray(new FieldType[params.size()]);
		index++;
		returnType = new FieldType(descriptor.substring(index));
	}

	public final void addDependentClassNames(Set<String> classNames) {
		for (FieldType parameterType : parameterTypes) {
			parameterType.addDependentClassNames(classNames);
		}
		returnType.addDependentClassNames(classNames);
	}

	public FieldType[] getParameterTypes() {
		return parameterTypes;
	}

	public FieldType getReturnType() {
		return returnType;
	}
}
