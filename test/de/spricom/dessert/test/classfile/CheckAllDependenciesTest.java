package de.spricom.dessert.test.classfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fest.assertions.Assertions;
import org.junit.Test;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.traversal.ClassVisitor;
import de.spricom.dessert.traversal.PathProcessor;

public class CheckAllDependenciesTest implements ClassVisitor {
	private static final Pattern JDEPS_REGEX = Pattern.compile("^\\s+-> ([\\w.$]+)\\s*(\\S+)?$");
	
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
		proc.traverseAllClasses(this);
	}
	
	@Override
	public void visit(File root, String classname, InputStream content) {
		try {
			ClassFile cf = new ClassFile(content);
			Set<String> deps1 = cf.getDependentClasses();
			Set<String> deps2 = getDependenciesUsingJDeps(root, classname);
			TreeSet<String> diff = new TreeSet<>(deps2);
			diff.removeAll(deps1);
			Assertions.assertThat(diff).as(classname).isEmpty();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		} catch (IOException ex) {
			throw new RuntimeException("Processing " + classname + " in " + root.getAbsolutePath() + " failed.", ex);
		}
	}
	
	private Set<String> getDependenciesUsingJDeps(File root, String className) throws IOException, InterruptedException {
		Set<String> dependencies = new TreeSet<>();
		ProcessBuilder pb = new ProcessBuilder("jDeps", "-cp", root.getAbsolutePath(), "-verbose:class", className);
		pb.redirectErrorStream(true);
		Process p = pb.start();
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		boolean deps = false;
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
			if (line.contains(className)) {
				deps = true;
				continue;
			}
			if (deps) {
				Matcher m = JDEPS_REGEX.matcher(line);
				if (m.matches()) {
					dependencies.add(m.group(1));
				} else {
					// Have to consume the whole stream for waitFor() to finish, thus we can't break here.
					deps = false;
				}
			}
		}
		p.waitFor();
		return dependencies;
	}

}
