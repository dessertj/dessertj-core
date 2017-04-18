package de.spricom.dessert.test.classfile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.traversal.ClassVisitor;
import de.spricom.dessert.traversal.PathProcessor;

public class DumpMostDependencies implements ClassVisitor {
	
	static class ClassRecord implements Comparable<ClassRecord> {
		File root;
		String classname;
		int depCount;
		
		ClassRecord(File root, String classname, ClassFile cf) {
			this.root = root;
			this.classname = classname;
			this.depCount = cf.getDependentClasses().size();
		}

		@Override
		public int compareTo(ClassRecord o) {
			int c = o.depCount - depCount;
			if (c != 0) {
				return c;
			}
			c = classname.compareTo(o.classname);
			if (c != 0) {
				return c;
			}
			if (o.depCount == depCount) {
				if (o.classname.equals(classname)) {
					
				}
			}
			return root.compareTo(o.root);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((classname == null) ? 0 : classname.hashCode());
			result = prime * result + ((root == null) ? 0 : root.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ClassRecord other = (ClassRecord) obj;
			if (classname == null) {
				if (other.classname != null)
					return false;
			} else if (!classname.equals(other.classname))
				return false;
			if (root == null) {
				if (other.root != null)
					return false;
			} else if (!root.equals(other.root))
				return false;
			return true;
		}
		
		public String toString() {
			return String.format("%5d depedencies: %s [%s]", depCount, classname, root.getPath());
		}
	}

	private static final int MAX_RECORDS = 1000;
	private int classesCount;
	private SortedSet<ClassRecord> classRecords = new TreeSet<>();
	
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
		
		for (ClassRecord rec : classRecords) {
			System.out.println(rec);
		}
	}

	public void dumpSystemProperties() {
		for (Object key : System.getProperties().keySet()) {
			System.out.println(key + " := " + System.getProperty((String) key)); 
		}
	}
	
	@Override
	public void visit(File root, String classname, InputStream content) {
		try {
			ClassFile cf = new ClassFile(content);
			classesCount++;
			classRecords.add(new ClassRecord(root, classname, cf));
			if (classRecords.size() > MAX_RECORDS) {
				classRecords.remove(classRecords.last());
			}
		} catch (IOException ex) {
			throw new RuntimeException("Processing " + classname + " in " + root.getAbsolutePath() + " failed.", ex);
		}
	}
}
