package de.spricom.dessert.dependency;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.traversal.ClassVisitor;

public class Repository implements ClassVisitor {
    private static final Logger log = Logger.getLogger(Repository.class.getName());
    private final Map<String, ClassFileEntry> scannedClasses = new HashMap<>();
    private final Map<Rule, DependencyUnit> dependencyUnitCache = new HashMap<>();

    @Override
    public void visit(File root, String classname, InputStream content) {
        try {
            ClassFileEntry entry = new ClassFileEntry(this, root, new ClassFile(content), scannedClasses.get(classname));
            assert classname.equals(entry.getClassName()) : "'" + classname + "' != " + entry.getClassName();
            scannedClasses.put(classname, entry);
        } catch (IOException ex) {
            log.log(Level.WARNING, "Processing " + classname + " in " + root.getAbsolutePath(), ex);
        }
    }

    public ClassFileEntry lookup(String classname) {
        ClassFileEntry entry = scannedClasses.get(classname);
        if (entry == null) {
            return null;
        }
        return entry;
    }
    
    public DependencyUnit getDependencyUnit(Rule rule) {
        DependencyUnit unit = dependencyUnitCache.get(rule);
        if (unit == null) {
            unit = new DependencyUnit(findMatchingClasses(rule));
            unit.setName(rule.toString());
            dependencyUnitCache.put(rule, unit);
        }
        return unit;
    }
    
    private Map<String, ClassFileEntry> findMatchingClasses(Rule rule) {
        Map<String, ClassFileEntry> members = new HashMap<>();
        for (ClassFileEntry entry : scannedClasses.values()) {
            if (!members.containsKey(entry.getClassName()) && rule.isMember(entry)) {
                members.put(entry.getClassName(), entry);
            }
        }
        return members;
    }
}
