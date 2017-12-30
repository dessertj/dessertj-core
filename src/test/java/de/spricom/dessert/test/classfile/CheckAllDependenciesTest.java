package de.spricom.dessert.test.classfile;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.traversal.ClassVisitor;
import de.spricom.dessert.traversal.PathProcessor;
import org.fest.assertions.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckAllDependenciesTest implements ClassVisitor {
    private static final Pattern JDEPS_REGEX = Pattern.compile("^\\s+-> ([\\w.$]+)\\s*(\\S+)?$");

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

    private void check(PathProcessor proc) throws IOException {
        proc.traverseAllClasses(this);
    }

    @Override
    public void visit(File root, String classname, InputStream content) {
        try {
            ClassFile cf = new ClassFile(content);
            Set<String> deps1 = cf.getDependentClasses();
            Set<String> deps2 = getDependenciesUsingJDeps(root, classname);
            TreeSet<String> diff = new TreeSet<String>(deps2);
            diff.removeAll(deps1);
            if (!diff.isEmpty()) {
                System.out.println(classname + ": " + diff);
            }
            Assertions.assertThat(diff).as(classname).isEmpty();
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
