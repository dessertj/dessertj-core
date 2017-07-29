package de.spricom.dessert.dependency;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.spricom.dessert.classfile.ClassFile;

public class ClassFileEntry {
    private final Repository repository;
    private final File root;
    private final ClassFile classFile;
    private final ClassFileEntry next;

    public ClassFileEntry(Repository repository, File root, ClassFile classFile, ClassFileEntry other) {
        this.repository = Objects.requireNonNull(repository, "repositry");
        this.root = Objects.requireNonNull(root, "root");
        this.classFile = Objects.requireNonNull(classFile, "classFile");
        this.next = other;
        if (other != null) {
            assert classFile.getThisClass().equals(other.classFile.getThisClass()) : "classname different";
            assert !root.equals(other.root) : "same root";
        }
    }

    public boolean isUnique() {
        return repository.lookup(getClassName()).next == null;
    }
    
    public List<ClassFileEntry> getAlternatives() {
        List<ClassFileEntry> list = new LinkedList<>();
        ClassFileEntry entry = repository.lookup(getClassName());
        while (entry != null) {
            list.add(entry);
            entry = entry.next;
        }
        return list;
    }
    
    public String getClassName() {
        return classFile.getThisClass();
    }
    
    public Set<String> getDependentClassNames() {
        return classFile.getDependentClasses();
    }
    
    public ClassFileEntry lookup(String classname) {
        return repository.lookup(classname);
    }

    @Override
    public String toString() {
        return getClassName();
    }

    @Override
    public int hashCode() {
        return getClassName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ClassFileEntry other = (ClassFileEntry) obj;
        return getClassName().equals(other.getClassName());
    }
}
