package de.spricom.dessert.test.classfile;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.traversal.ClassVisitor;
import de.spricom.dessert.traversal.PathProcessor;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fest.assertions.Assertions.assertThat;

public class CheckAllDependenciesTest implements ClassVisitor {
    private static final Pattern JDEPS_REGEX = Pattern.compile("^\\s+-> ([\\w.$]+)\\s*(\\S+)?(not found)?$");

    @Ignore
    @Test
    public void test() throws IOException {
        PathProcessor proc = new PathProcessor();
        check(proc);
    }

    @Ignore
    @Test
    public void testJdk() throws IOException {
        PathProcessor proc = new PathProcessor();
        proc.setPath(System.getProperty("sun.boot.class.path"));
        check(proc);
    }

    @Test
    public void testDessert() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String pathElement : System.getProperty("java.class.path").split(File.pathSeparator)) {
            if (!pathElement.endsWith(".jar")) {
                if (sb.length() > 0) {
                    sb.append(File.pathSeparator);
                }
                sb.append(pathElement);
            }
        }
        PathProcessor proc = new PathProcessor();
        proc.setPath(sb.toString());
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
            assertThat(deps1).as("Dependencies of " + classname).isEqualTo(deps2);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (IOException ex) {
            throw new RuntimeException("Processing " + classname + " in " + root.getAbsolutePath() + " failed.", ex);
        }
    }

    private Set<String> getDependenciesUsingJDeps(File root, String className) throws IOException, InterruptedException {
        Set<String> dependencies = new TreeSet<String>();
        ProcessBuilder pb = new ProcessBuilder("jdeps", "-cp", root.getAbsolutePath(), "-verbose:class", className);
        pb.redirectErrorStream(true);
        System.out.println("\n" + pb.command());
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
