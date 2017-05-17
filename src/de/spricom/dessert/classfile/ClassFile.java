package de.spricom.dessert.classfile;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import de.spricom.dessert.classfile.attribute.AttributeInfo;

public class ClassFile {
	public static final int MAGIC = 0xCAFEBABE;

	public static final int ACC_PUBLIC = 0x0001; // Declared public; may be
													// accessed from outside its
													// package.
	public static final int ACC_FINAL = 0x0010; // Declared final; no subclasses
												// allowed.
	public static final int ACC_SUPER = 0x0020; // Treat superclass methods
												// specially when invoked by the
												// invokespecial instruction.
	public static final int ACC_INTERFACE = 0x0200; // Is an interface, not a
													// class.
	public static final int ACC_ABSTRACT = 0x0400; // Declared abstract; must
													// not be instantiated.
	public static final int ACC_SYNTHETIC = 0x1000; // Declared synthetic; not
													// present in the source
													// code.
	public static final int ACC_ANNOTATION = 0x2000; // Declared as an
														// annotation type.
	public static final int ACC_ENUM = 0x4000; // Declared as an enum type.

	private int minorVersion;
	private int majorVersion;
	private ConstantPoolEntry[] constantPool;
	private int accessFlags;
	private String thisClass;
	private String superClass;
	private String[] interfaces;
	private FieldInfo[] fields;
	private MethodInfo[] methods;
	private AttributeInfo[] attributes;

	public ClassFile(Class<?> clazz) throws IOException {
		this(open(clazz));
	}

	private static InputStream open(Class<?> clazz) throws IOException {
		if (clazz.getDeclaringClass() != null) {
			clazz = clazz.getDeclaringClass();
		}
		return Objects.requireNonNull(clazz.getResourceAsStream(clazz.getSimpleName() + ".class"),
				"No class file found for " + clazz);
	}

	public ClassFile(InputStream in) throws IOException {
		try (BufferedInputStream bi = new BufferedInputStream(in)) {
			try (DataInputStream is = new DataInputStream(bi)) {
				if (ClassFile.MAGIC != is.readInt()) {
					throw new IOException("Not a class file.");
				}
				minorVersion = is.readUnsignedShort();
				majorVersion = is.readUnsignedShort();
				readConstantPool(is);
				accessFlags = is.readUnsignedShort();
				thisClass = getConstantClassName(is.readUnsignedShort());
				superClass = getConstantClassName(is.readUnsignedShort());
				readInterfaces(is);
				readFields(is);
				readMethods(is);
				attributes = AttributeInfo.readAttributes(is, constantPool);
				if (is.read() != -1) {
					throw new IOException("EOF not reached!");
				}
			}
		}
	}

	private void readConstantPool(DataInputStream is) throws IOException {
		int constantPoolCount = is.readUnsignedShort();
		constantPool = new ConstantPoolEntry[constantPoolCount];
		int index = 1;
		while (index < constantPoolCount) {
			int tag = is.readUnsignedByte();
			switch (tag) {
			case ConstantUtf8.TAG:
				constantPool[index] = new ConstantUtf8(is.readUTF());
				break;
			case ConstantInteger.TAG:
				constantPool[index] = new ConstantInteger(is.readInt());
				break;
			case ConstantFloat.TAG:
				constantPool[index] = new ConstantFloat(is.readFloat());
				break;
			case ConstantLong.TAG:
				constantPool[index] = new ConstantLong(is.readLong());
				index++;
				break;
			case ConstantDouble.TAG:
				constantPool[index] = new ConstantDouble(is.readDouble());
				index++;
				break;
			case ConstantClass.TAG:
				constantPool[index] = new ConstantClass(is.readUnsignedShort());
				break;
			case ConstantString.TAG:
				constantPool[index] = new ConstantString(is.readUnsignedShort());
				break;
			case ConstantFieldref.TAG:
				constantPool[index] = new ConstantFieldref(is.readUnsignedShort(), is.readUnsignedShort());
				break;
			case ConstantMethodref.TAG:
				constantPool[index] = new ConstantMethodref(is.readUnsignedShort(), is.readUnsignedShort());
				break;
			case ConstantInterfaceMethodref.TAG:
				constantPool[index] = new ConstantInterfaceMethodref(is.readUnsignedShort(), is.readUnsignedShort());
				break;
			case ConstantNameAndType.TAG:
				constantPool[index] = new ConstantNameAndType(is.readUnsignedShort(), is.readUnsignedShort());
				break;
			case ConstantMethodHandle.TAG:
				constantPool[index] = new ConstantMethodHandle(is.readUnsignedByte(), is.readUnsignedShort());
				break;
			case ConstantMethodType.TAG:
				constantPool[index] = new ConstantMethodType(is.readUnsignedShort());
				break;
			case ConstantInvokeDynamic.TAG:
				constantPool[index] = new ConstantInvokeDynamic(is.readUnsignedShort(), is.readUnsignedShort());
				break;
			default:
				throw new IOException("Unknown constant-pool tag: " + tag);
			}
			index++;
		}
	}

	private String getConstantClassName(int index) {
		ConstantClass clazz = (ConstantClass) constantPool[index];
		if (clazz == null) {
			return null;
		}
		return clazz.getName(this);
	}

	private void readInterfaces(DataInputStream is) throws IOException {
		int interfacesCount = is.readUnsignedShort();
		interfaces = new String[interfacesCount];
		for (int i = 0; i < interfacesCount; i++) {
			interfaces[i] = getConstantClassName(is.readUnsignedShort());
		}
	}

	private void readFields(DataInputStream is) throws IOException {
		int fieldCount = is.readUnsignedShort();
		fields = new FieldInfo[fieldCount];
		for (int i = 0; i < fieldCount; i++) {
			fields[i] = new FieldInfo();
			readMember(fields[i], is);
		}
	}

	private void readMethods(DataInputStream is) throws IOException {
		int methodCount = is.readUnsignedShort();
		methods = new MethodInfo[methodCount];
		for (int i = 0; i < methodCount; i++) {
			methods[i] = new MethodInfo();
			readMember(methods[i], is);
		}
	}

	private void readMember(MemberInfo member, DataInputStream is) throws IOException {
		member.setAccessFlags(is.readUnsignedShort());
		member.setName(readString(is));
		member.setDescriptor(readString(is));
		member.setAttributes(AttributeInfo.readAttributes(is, constantPool));
	}

	private String readString(DataInputStream is) throws IOException {
		return ((ConstantUtf8) constantPool[is.readUnsignedShort()]).getValue();
	}

	public ConstantPoolEntry getConstantPoolEntry(int index) {
		return constantPool[index];
	}
	
	public Set<String> getDependentClasses() {
		Set<String> classNames = new TreeSet<>();
		for (int i = 1; i < constantPool.length; i++) {
			if (constantPool[i] != null) {
				constantPool[i].addDependendClassNames(classNames, this);
			}
		}
		for (FieldInfo fieldInfo : fields) {
			fieldInfo.addDependendClassNames(classNames);
		}
		for (MethodInfo methodInfo : methods) {
			methodInfo.addDependendClassNames(classNames);
		}
		for (AttributeInfo attribute : attributes) {
			attribute.addDependendClassNames(classNames);
		}
		classNames.remove(thisClass);
		return classNames;
	}

	public String dumpConstantPool() {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (ConstantPoolEntry entry : constantPool) {
			if (entry != null) {
				sb.append(String.format("%4d: %s%n", index, entry.dump(this)));
			}
			index++;
		}
		return sb.toString();
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public ConstantPoolEntry[] getConstantPool() {
		return constantPool;
	}

	public int getAccessFlags() {
		return accessFlags;
	}

	public String getThisClass() {
		return thisClass;
	}

	public String getSuperClass() {
		return superClass;
	}

	public String[] getInterfaces() {
		return interfaces;
	}

	public FieldInfo[] getFields() {
		return fields;
	}

	public MethodInfo[] getMethods() {
		return methods;
	}

	public AttributeInfo[] getAttributes() {
		return attributes;
	}

	public boolean isPublic() {
		return (accessFlags & ACC_PUBLIC) != 0;
	}

	public boolean isFinal() {
		return (accessFlags & ACC_FINAL) != 0;
	}

	public boolean isSuper() {
		return (accessFlags & ACC_SUPER) != 0;
	}

	public boolean isInterface() {
		return (accessFlags & ACC_INTERFACE) != 0;
	}

	public boolean isAbstract() {
		return (accessFlags & ACC_ABSTRACT) != 0;
	}

	public boolean isSynthetic() {
		return (accessFlags & ACC_SYNTHETIC) != 0;
	}

	public boolean isAnnotation() {
		return (accessFlags & ACC_ANNOTATION) != 0;
	}

	public boolean isEnum() {
		return (accessFlags & ACC_ENUM) != 0;
	}
}
