package de.spricom.dessert.test.resolve;

import de.spricom.dessert.resolve.ClassCollector;
import de.spricom.dessert.resolve.ClassPackage;
import de.spricom.dessert.resolve.ClassRoot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FakeRoot extends ClassRoot {
    private Map<String, ClassPackage> packages;
    private ClassCollector collector;

    public FakeRoot(File file) {
        super(file);
    }

    @Override
    protected void scan(ClassCollector collector) {
        this.collector = collector;
        packages = new HashMap<String, ClassPackage>();
        collector.addPackage(this);
        packages.put("", this);
    }

    public void add(String classname) {
        ClassPackage pckg = ensurePackage(parentPackageName(classname));
        FakeClassEntry ce = new FakeClassEntry(classname, pckg);
        pckg.addClass(ce);
        collector.addClass(ce);
    }

    private ClassPackage ensurePackage(String packageName) {
        ClassPackage pckg = packages.get(packageName);
        if (pckg != null) {
            return pckg;
        }
        ClassPackage parent = ensurePackage(parentPackageName(packageName));
        pckg = new ClassPackage(parent, packageName);
        collector.addPackage(pckg);
        packages.put(packageName, pckg);
        return pckg;
    }

    private String parentPackageName(String packageName) {
        int index = packageName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        String parentPackageName = packageName.substring(0, index);
        return parentPackageName;
    }
}
