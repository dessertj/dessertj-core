package de.spricom.dessert.jdeps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Uses jdeps to determine all dependencies of all classes within some root (jar file or directory).
 * Supports both the jdeps command of JDK-8 and of JDK-9.
 */
public class JdepsWrapper {
    // The '-' appears in package-info.class, '$' is required for inner classes.
    private static final Pattern SRC_REGEX = Pattern.compile("^\\s+([\\w.$-]+)\\s+.*");
    private static final Pattern DEST_REGEX = Pattern.compile("^.*-> ([\\w.$]+)\\s+.*$");

    private String classPath = System.getProperty("java.class.path");
    private String jdepsCommand = "jdeps";
    private List<String> options = new ArrayList<String>(Arrays.asList("-verbose:class", "-filter:none"));
    private File path;
    private boolean verbose = false;

    private Map<String, Set<String>> dependencyMap;

    public JdepsWrapper(File path) {
        this.path = path;
    }

    public Set<String> getClasses() {
        ensureDependencies();
        return dependencyMap.keySet();
    }

    public Set<String> getDependencies(String classname) {
        ensureDependencies();
        return dependencyMap.get(classname);
    }

    private void ensureDependencies() {
        if (dependencyMap == null) {
            throw new IllegalStateException("No dependencyMap analyzed, refresh() required");
        }
    }

    protected List<String> getCommand() {
        List<String> command = new ArrayList<String>();
        command.add(jdepsCommand);
        command.addAll(options);
        command.add("-cp");
        command.add(classPath);
        command.add(path.getPath());
        return command;
    }

    public void refresh() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(getCommand());
        pb.redirectErrorStream(true);
        dependencyMap = new HashMap<String, Set<String>>();
        Process p = pb.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        String currentClass = "none";
        while ((line = in.readLine()) != null) {
            if (verbose) {
                System.out.println(line);
            }
            Matcher src = SRC_REGEX.matcher(line);
            if (src.matches()) {
                currentClass = src.group(1);
            }
            Matcher dest = DEST_REGEX.matcher(line);
            if (dest.matches()) {
                String dependentClass = dest.group(1);
                addDependency(currentClass, dependentClass);
            }
        }
        p.waitFor();
    }

    private void addDependency(String currentClass, String dependentClass) {
        Set<String> dependencies = dependencyMap.get(currentClass);
        if (dependencies == null) {
            dependencies = new TreeSet<String>();
            dependencyMap.put(currentClass, dependencies);
        }
        dependencies.add(dependentClass);
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getJdepsCommand() {
        return jdepsCommand;
    }

    public void setJdepsCommand(String jdepsCommand) {
        this.jdepsCommand = jdepsCommand;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
