package de.spricom.dessert.classfile.constpool;

import de.spricom.dessert.classfile.dependency.DependencyHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class MethodType  implements DependencyHolder {
	private static final Pattern DESCRIPTOR_PATTERN = Pattern
			.compile("\\((\\[*([BCDEFIJSZ]|L\\S+;))*\\)\\[*([BCDEFIJSVZ]|L\\S+;)");

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

	public static boolean isMethodDescriptor(String descriptor) {
		return DESCRIPTOR_PATTERN.matcher(descriptor).matches();
	}

	public FieldType[] getParameterTypes() {
		return parameterTypes;
	}

	public FieldType getReturnType() {
		return returnType;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		for (FieldType parameterType : parameterTypes) {
			if (sb.length() > 1) {
				sb.append(", ");
			}
			sb.append(parameterType);
		}
		sb.append(") -> ");
		sb.append(returnType);
		return sb.toString();
	}
}
