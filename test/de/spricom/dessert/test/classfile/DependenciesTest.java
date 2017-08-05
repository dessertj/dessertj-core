package de.spricom.dessert.test.classfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fest.assertions.Assertions;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;

import de.spricom.dessert.classfile.ClassFile;

public class DependenciesTest {
	private static final Pattern JDEPS_REGEX = Pattern.compile("^\\s+-> ([\\w.$]+)\\s*(\\S+)?$");
	private String jDepsClassPath = System.getProperty("java.class.path").replace('\\', '/').replace(";/", ";");

	public void testJDeps() throws IOException, InterruptedException {
		// ProcessBuilder pb = new ProcessBuilder("jDeps", "-cp",
		// "C:/Users/Hajo/.gradle/caches/modules-2/files-2.1/net.sourceforge.htmlunit/htmlunit/2.19/230f011fa87e96ff4115cd8c9d1572572d718b3e/htmlunit-2.19.jar",
		// "-verbose:class",
		// "com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration");
		ProcessBuilder pb = new ProcessBuilder("jdeps", "-cp",
				System.getProperty("java.class.path").replace('\\', '/').replace(";/", ";"), "-verbose:class",
				"com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration");
		pb.redirectErrorStream(true);
		Process p = pb.start();
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		int count = 0;
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
			if (line.startsWith("      -> ")) {
				count++;
			}
		}
		p.waitFor();
		System.out.println(count + " total dependencies");
	}

	public void dumpSystemProperties() {
		for (Object key : System.getProperties().keySet()) {
			System.out.println(key + " := " + System.getProperty((String) key));
		}
	}
	
	@Test
	public void testGetDependencies() throws IOException, InterruptedException {
        Class<?> clazz = this.getClass();
		ClassFile cf = new ClassFile(clazz);
		Set<String> deps1 = cf.getDependentClasses();
		Set<String> deps2 = getDependenciesUsingJDeps(clazz.getName());
		deps2.add(clazz.getSuperclass().getName());
		TreeSet<String> diff1 = new TreeSet<>(deps2);
		diff1.removeAll(deps1);
        TreeSet<String> diff2 = new TreeSet<>(deps1);
        diff2.removeAll(deps2);
		Assertions.assertThat(diff1).isEmpty();
        Assertions.assertThat(diff2).isEmpty();
		Assertions.assertThat(deps1).hasSize(deps2.size());
        Assertions.assertThat(deps1).isEqualTo(deps2);
	}
	
	private Set<String> getDependenciesUsingJDeps(String className) throws IOException, InterruptedException {
		Set<String> dependencies = new TreeSet<>();
		ProcessBuilder pb = new ProcessBuilder("jdeps", "-cp", jDepsClassPath, "-verbose:class", className);
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
