package de.spricom.dessert.classfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MethodType {
	private final FieldType[] parameterTypes;
	private final FieldType returnType;
	
	public MethodType(String descriptor) {
		Objects.requireNonNull(descriptor, "descriptor == null");
		if ('(' != descriptor.charAt(0)) {
			throw new IllegalArgumentException("Invalid method descriptor: " + descriptor);
		}
		List<FieldType> params = new ArrayList<>();
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

	public FieldType[] getParameterTypes() {
		return parameterTypes;
	}

	public FieldType getReturnType() {
		return returnType;
	}
}
