package de.spricom.dessert.resolve;

import java.util.HashMap;
import java.util.Map;

class ClassResolverCache implements ClassCollector {
    private final Map<String, ClassPackage> packages = new HashMap<String, ClassPackage>(3000);
    private final Map<String, ClassEntry> classes = new HashMap<String, ClassEntry>(60000);

    @Override
    public void addPackage(ClassPackage pckg) {
        String pn = pckg.getPackageName();
        ClassPackage prev = packages.get(pn);
        if (prev == null) {
            packages.put(pn, pckg);
        } else {
            prev.addAlternative(pckg);
        }
    }

    @Override
    public void addClass(ClassEntry ce) {
        String cn = ce.getClassname();
        ClassEntry prev = classes.get(cn);
        if (prev == null) {
            classes.put(cn, ce);
        } else {
            prev.addAlternative(ce);
        }
    }

    ClassPackage getPackage(String packageName) {
        return packages.get(packageName);
    }

    ClassEntry getClassEntry(String classname) {
        return classes.get(classname);
    }

    int getPackageCount() {
        return packages.size();
    }

    int getClassCount() {
        return classes.size();
    }
}
