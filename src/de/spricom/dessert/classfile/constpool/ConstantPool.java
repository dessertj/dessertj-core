package de.spricom.dessert.classfile.constpool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

import de.spricom.dessert.classfile.dependency.DependencyHolder;

public final class ConstantPool implements DependencyHolder {
	private final ConstantPoolEntry[] entries;

	public ConstantPool(DataInputStream is) throws IOException {
		entries = new ConstantPoolEntry[is.readUnsignedShort()];
		int index = 1;
		while (index < entries.length) {
			int offset = 1;
			int tag = is.readUnsignedByte();
			switch (tag) {
			case ConstantUtf8.TAG:
				entries[index] = new ConstantUtf8(is.readUTF());
				break;
			case ConstantInteger.TAG:
				entries[index] = new ConstantInteger(is.readInt());
				break;
			case ConstantFloat.TAG:
				entries[index] = new ConstantFloat(is.readFloat());
				break;
			case ConstantLong.TAG:
				entries[index] = new ConstantLong(is.readLong());
				offset = 2;
				break;
			case ConstantDouble.TAG:
				entries[index] = new ConstantDouble(is.readDouble());
				offset = 2;
				break;
			case ConstantClass.TAG:
				entries[index] = new ConstantClass(is.readUnsignedShort());
				break;
			case ConstantString.TAG:
				entries[index] = new ConstantString(is.readUnsignedShort());
				break;
			case ConstantFieldref.TAG:
				entries[index] = new ConstantFieldref(is.readUnsignedShort(), is.readUnsignedShort());
				break;
			case ConstantMethodref.TAG:
				entries[index] = new ConstantMethodref(is.readUnsignedShort(), is.readUnsignedShort());
				break;
			case ConstantInterfaceMethodref.TAG:
				entries[index] = new ConstantInterfaceMethodref(is.readUnsignedShort(), is.readUnsignedShort());
				break;
			case ConstantNameAndType.TAG:
				entries[index] = new ConstantNameAndType(is.readUnsignedShort(), is.readUnsignedShort());
				break;
			case ConstantMethodHandle.TAG:
				entries[index] = new ConstantMethodHandle(is.readUnsignedByte(), is.readUnsignedShort());
				break;
			case ConstantMethodType.TAG:
				entries[index] = new ConstantMethodType(is.readUnsignedShort());
				break;
			case ConstantInvokeDynamic.TAG:
				entries[index] = new ConstantInvokeDynamic(is.readUnsignedShort(), is.readUnsignedShort());
				break;
			default:
				throw new IOException("Unknown constant-pool tag: " + tag);
			}
			entries[index].setConstantPool(this);
			index += offset;
		}
	}

	public String dumpConstantPool() {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (ConstantPoolEntry entry : entries) {
			if (entry != null) {
				sb.append(String.format("%4d: %s%n", index, entry.dump()));
			}
			index++;
		}
		return sb.toString();
	}

	ConstantPoolEntry getEntry(int index) {
		return entries[index];
	}

	public String getUtf8String(int index) {
		ConstantUtf8 entry = (ConstantUtf8) entries[index];
		return entry.getValue();
	}

	@SuppressWarnings("unchecked")
	public <T> T getConstantValue(int index) {
		return (T) entries[index];
	}

	public FieldType getFieldType(int index) {
		return new FieldType(getUtf8String(index));
	}

	public String getConstantClassName(int index) {
		ConstantClass clazz = (ConstantClass) entries[index];
		if (clazz == null) {
			return null;
		}
		return clazz.getName();
	}

	@Override
	public void addDependentClassNames(Set<String> classNames) {
		for (ConstantPoolEntry entry : entries) {
			if (entry != null) {
				entry.addDependentClassNames(classNames);
			}
		}
	}
}
