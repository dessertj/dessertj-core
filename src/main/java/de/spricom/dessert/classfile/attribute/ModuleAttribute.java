package de.spricom.dessert.classfile.attribute;

/*-
 * #%L
 * Dessert Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2022 Hans Jörg Heßmann
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import de.spricom.dessert.classfile.constpool.ConstantPool;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Represents a
 * <a href="https://docs.oracle.com/javase/specs/jvms/se19/html/jvms-4.html#jvms-4.7.25" target="_blank">
 * Java Virtual Machine Specification: 4.7.25. The Module Attribute</a>.
 */
public class ModuleAttribute extends AttributeInfo {
	// Module flags
	public static final int ACC_OPEN = 0x0020; // Indicates that this module is open.
	public static final int ACC_SYNTHETIC = 0x1000; // Indicates that this module was not explicitly or implicitly declared.
	public static final int ACC_MANDATED = 0x8000; // Indicates that this module was implicitly declared.

	// Requires flags
	public static final int ACC_TRANSITIVE = 0x0020; // Indicates that any module which depends on the current module, implicitly declares a dependence on the module indicated by this entry.
	public static final int ACC_STATIC_PHASE = 0x0040; // Indicates that this dependence is mandatory in the static phase, i.e., at compile time, but is optional in the dynamic phase, i.e., at run time.

	public static class Require {
		private final String moduleName;
		private final int flags;
		private final String version;

		private Require(DataInputStream is, ConstantPool constantPool) throws IOException {
			moduleName = constantPool.getModuleName(is.readUnsignedShort());
			flags = is.readUnsignedShort();
			int versionIndex = is.readUnsignedShort();
			version = versionIndex == 0 ? null : constantPool.getUtf8String(versionIndex);
		}

		public String getModuleName() {
			return moduleName;
		}

		public int getFlags() {
			return flags;
		}

		public String getVersion() {
			return version;
		}

		public boolean isTransitive() {
			return (flags & ACC_TRANSITIVE) != 0;
		}

		public boolean isStaticPhase() {
			return (flags & ACC_STATIC_PHASE) != 0;
		}

		public boolean isSynthetic() {
			return (flags & ACC_SYNTHETIC) != 0;
		}

		public boolean isMandated() {
			return (flags & ACC_MANDATED) != 0;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			appendString(sb);
			return sb.toString();
		}

		void appendString(StringBuilder sb) {
			sb.append("requires ");
			if (isMandated()) {
				sb.append("mandated ");
			}
			if (isSynthetic()) {
				sb.append("synthetic ");
			}
			if (isTransitive()) {
				sb.append("transitive ");
			}
			if (isStaticPhase()) {
				sb.append("static ");
			}
			sb.append(moduleName);
			if (version != null) {
				sb.append("[").append(version).append("]");
			}
		}
	}

	public static class Export {
		private final String packageName;
		private final int flags;
		private final String[] exportsTo;

		private Export(DataInputStream is, ConstantPool constantPool) throws IOException {
			packageName = constantPool.getPackageName(is.readUnsignedShort());
			flags = is.readUnsignedShort();
			exportsTo = new String[is.readUnsignedShort()];
			for (int i = 0; i < exportsTo.length; i++) {
				exportsTo[i] = constantPool.getModuleName(is.readUnsignedShort());
			}
		}

		public String getPackageName() {
			return packageName;
		}

		public int getFlags() {
			return flags;
		}

		public String[] getExportsTo() {
			return exportsTo;
		}

		public boolean isUnqualified() {
			return exportsTo.length == 0;
		}

		public boolean isSynthetic() {
			return (flags & ACC_SYNTHETIC) != 0;
		}

		public boolean isMandated() {
			return (flags & ACC_MANDATED) != 0;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			appendString(sb);
			return sb.toString();
		}

		void appendString(StringBuilder sb) {
			sb.append("exports ");
			if (isMandated()) {
				sb.append("mandated ");
			}
			if (isSynthetic()) {
				sb.append("synthetic ");
			}
			sb.append(packageName);
			String separator = " to ";
			for (String to : exportsTo) {
				sb.append(separator).append(to);
				separator = ", ";
			}
		}
	}

	public static class Open {
		private final String packageName;
		private final int flags;
		private final String[] opensTo;

		private Open(DataInputStream is, ConstantPool constantPool) throws IOException {
			packageName = constantPool.getPackageName(is.readUnsignedShort());
			flags = is.readUnsignedShort();
			opensTo = new String[is.readUnsignedShort()];
			for (int i = 0; i < opensTo.length; i++) {
				opensTo[i] = constantPool.getModuleName(is.readUnsignedShort());
			}
		}

		public String getPackageName() {
			return packageName;
		}

		public int getFlags() {
			return flags;
		}

		public String[] getOpensTo() {
			return opensTo;
		}

		public boolean isUnqualified() {
			return opensTo.length == 0;
		}

		public boolean isSynthetic() {
			return (flags & ACC_SYNTHETIC) != 0;
		}

		public boolean isMandated() {
			return (flags & ACC_MANDATED) != 0;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			appendString(sb);
			return sb.toString();
		}

		void appendString(StringBuilder sb) {
			sb.append("opens ");
			if (isMandated()) {
				sb.append("mandated ");
			}
			if (isSynthetic()) {
				sb.append("synthetic ");
			}
			sb.append(packageName);
			String separator = " to ";
			for (String to : opensTo) {
				sb.append(separator).append(to);
				separator = ", ";
			}
		}
	}

	public static class Use {
		private final String className;

		private Use(DataInputStream is, ConstantPool constantPool) throws IOException {
			className = constantPool.getConstantClassName(is.readUnsignedShort());
		}

		public String getClassName() {
			return className;
		}

		public String toString() {
			return "uses " + className;
		}
	}

	public static class Provide {
		private final String className;
		private final String[] providesWith;

		private Provide(DataInputStream is, ConstantPool constantPool) throws IOException {
			className = constantPool.getConstantClassName(is.readUnsignedShort());
			providesWith = new String[is.readUnsignedShort()];
			for (int i = 0; i < providesWith.length; i++) {
				providesWith[i] = constantPool.getConstantClassName(is.readUnsignedShort());
			}
		}

		public String getClassName() {
			return className;
		}

		public String[] getProvidesWith() {
			return providesWith;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			appendString(sb);
			return sb.toString();
		}

		void appendString(StringBuilder sb) {
			sb.append("provides ");
			sb.append(className);
			String separator = " with ";
			for (String with : providesWith) {
				sb.append(separator).append(with);
				separator = ", ";
			}
		}
	}

	private final String moduleName;
	private final int moduleFlags;
	private final String moduleVersion;
	private final Require[] requires;
	private final Export[] exports;
	private final Open[] opens;
	private final Use[] uses;
	private final Provide[] provides;

    public ModuleAttribute(String name, DataInputStream is, ConstantPool constantPool) throws IOException {
		super(name);
		skipLength(is);
		moduleName = constantPool.getModuleName(is.readUnsignedShort());
		moduleFlags = is.readUnsignedShort();
		int versionIndex = is.readUnsignedShort();
		moduleVersion = versionIndex == 0 ? null : constantPool.getUtf8String(versionIndex);

		requires = new Require[is.readUnsignedShort()];
		for (int i = 0; i < requires.length; i++) {
			requires[i] = new Require(is, constantPool);
		}
		exports = new Export[is.readUnsignedShort()];
		for (int i = 0; i < exports.length; i++) {
			exports[i] = new Export(is, constantPool);
		}
		opens = new Open[is.readUnsignedShort()];
		for (int i = 0; i < opens.length; i++) {
			opens[i] = new Open(is, constantPool);
		}
		uses = new Use[is.readUnsignedShort()];
		for (int i = 0; i < uses.length; i++) {
			uses[i] = new Use(is, constantPool);
		}
		provides = new Provide[is.readUnsignedShort()];
		for (int i = 0; i < provides.length; i++) {
			provides[i] = new Provide(is, constantPool);
		}
	}

	public String getModuleName() {
		return moduleName;
	}

	public int getModuleFlags() {
		return moduleFlags;
	}

	public String getModuleVersion() {
		return moduleVersion;
	}

	public Require[] getRequires() {
		return requires;
	}

	public Export[] getExports() {
		return exports;
	}

	public Open[] getOpens() {
		return opens;
	}

	public Use[] getUses() {
		return uses;
	}

	public Provide[] getProvides() {
		return provides;
	}

	public boolean isOpen() {
		return (moduleFlags & ACC_OPEN) != 0;
	}

	public boolean isSynthetic() {
		return (moduleFlags & ACC_SYNTHETIC) != 0;
	}

	public boolean isMandated() {
		return (moduleFlags & ACC_MANDATED) != 0;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(":\n");
		if (isMandated()) {
			sb.append("mandated ");
		}
		if (isSynthetic()) {
			sb.append("synthetic ");
		}
		if (isOpen()) {
			sb.append("open ");
		}
		sb.append("module ");
		sb.append(moduleName);
		if (moduleVersion != null) {
			sb.append("[").append(moduleVersion).append("]");
		}
		sb.append(" {\n");
		for (Require require : requires) {
			sb.append("  ");
			require.appendString(sb);
			sb.append(";\n");
		}
		for (Export export : exports) {
			sb.append("  ");
			export.appendString(sb);
			sb.append(";\n");
		}
		for (Open open : opens) {
			sb.append("  ");
			open.appendString(sb);
			sb.append(";\n");
		}
		for (Use use : uses) {
			sb.append("  ").append(use).append(";\n");
		}
		for (Provide provide : provides) {
			sb.append("  ");
			provide.appendString(sb);
			sb.append(";\n");
		}
		sb.append("}\n");
		return sb.toString();
	}
}
