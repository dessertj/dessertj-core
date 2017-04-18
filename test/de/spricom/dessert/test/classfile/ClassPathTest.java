package de.spricom.dessert.test.classfile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.Test;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.FieldInfo;
import de.spricom.dessert.classfile.MethodInfo;
import de.spricom.dessert.traversal.ClassVisitor;
import de.spricom.dessert.traversal.PathProcessor;

public class ClassPathTest implements ClassVisitor {
	private int classesCount;
	private int maxDependentClasses;
	private ClassFile mostComplexClass;
	
	@Test
	public void test() throws IOException {
		PathProcessor proc = new PathProcessor();
		check(proc);
	}

	@Test
	public void testJdk() throws IOException {
		PathProcessor proc = new PathProcessor();
		proc.setPath(System.getProperty("sun.boot.class.path"));
		check(proc);
	}

	private void check(PathProcessor proc) throws IOException {
		long ts = System.currentTimeMillis();
		proc.traverseAllClasses(this);
		long delta = System.currentTimeMillis() - ts;
		if (classesCount == 0) {
			System.out.println("No classes found.");
			return;
		}
		System.out.println("Needed " + delta + " ms to traverse " + classesCount + " classes."
				+ " (" + (delta * 1000.0 / classesCount) + " µs/class)");
		System.out.println("Most complex class " + mostComplexClass.getThisClass() + " has " + maxDependentClasses + " dependencies:");
		dump(mostComplexClass);
	}

	public void dumpSystemProperties() {
		for (Object key : System.getProperties().keySet()) {
			System.out.println(key + " := " + System.getProperty((String) key)); 
		}
	}
	
	private void dump(ClassFile cf) {
		System.out.println(cf.getThisClass());
		System.out.println("extends " + cf.getSuperClass());
		System.out.println("implements " + Arrays.toString(cf.getInterfaces()));
		System.out.println("fields:");
		for (FieldInfo field : cf.getFields()) {
			System.out.print("  ");
			System.out.println(field.getDeclaration());
		}
		System.out.println("methods:");
		for (MethodInfo method : cf.getMethods()) {
			System.out.print("  ");
			System.out.println(method.getDeclaration());
		}
		System.out.println("depends:");
		cf.getDependentClasses().forEach(c -> System.out.println("  " + c));
	}

	@Override
	public void visit(File root, String classname, InputStream content) {
		try {
			ClassFile cf = new ClassFile(content);
			classesCount++;
			int depCount = cf.getDependentClasses().size();
			if (depCount > maxDependentClasses) {
				maxDependentClasses = depCount;
				mostComplexClass = cf;
			}
		} catch (IOException ex) {
			throw new RuntimeException("Processing " + classname + " in " + root.getAbsolutePath() + " failed.", ex);
		}
	}
}
