package de.spricom.dessert.slicing;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.spricom.dessert.resolve.ClassFileEntry;

public final class SliceEntry {
    private final SliceContext context;
    private final ClassFileEntry classfile;
    private Class<?> clazz;
    
    private SliceEntry superclass;
    private List<SliceEntry> implementedInterfaces;
    private List<SliceEntry> usedClasses;
    private List<SliceEntry> alternatives;

    SliceEntry(SliceContext context, ClassFileEntry classfile) {
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(classfile, "classfile");
        this.context = context;
        this.classfile = classfile;
    }

    public File getRootFile() {
        return classfile.getPackage().getRootFile();
    }
    
    public String getPackageName() {
        return classfile.getPackage().getPackageName();
    }
    
    public String getFilename() {
        return classfile.getFilename();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getRootFile().hashCode();
        result = prime * result + getPackageName().hashCode();
        result = prime * result + getFilename().hashCode();
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
        SliceEntry other = (SliceEntry) obj;
        if (!getRootFile().equals(other.getRootFile())) {
            return false;
        }
        if (!getPackageName().equals(other.getPackageName())) {
            return false;
        }
        if (!getFilename().equals(other.getFilename())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return classfile.getClassfile().getThisClass();
    }
    
    public Class<?> getClazz() throws ClassNotFoundException {
        if (clazz == null) {
            clazz = Class.forName(classfile.getClassfile().getThisClass());
        }
        return clazz;
    }

    public SliceEntry getSuperclass() {
        if (superclass == null) {
            superclass = context.getSliceEntry(classfile.getClassfile().getSuperClass());
        }
        return superclass;
    }

    public List<SliceEntry> getImplementedInterfaces() {
        if (implementedInterfaces == null) {
            implementedInterfaces = new ArrayList<SliceEntry>(classfile.getClassfile().getInterfaces().length);
            for (String in : classfile.getClassfile().getInterfaces()) {
                implementedInterfaces.add(context.getSliceEntry(in));
            }
        }
        return implementedInterfaces;
    }

    public List<SliceEntry> getUsedClasses() {
        if (usedClasses == null) {
            usedClasses = new ArrayList<SliceEntry>(classfile.getClassfile().getDependentClasses().size());
            for (String cn : classfile.getClassfile().getDependentClasses()) {
                usedClasses.add(context.getSliceEntry(cn));
            }
        }
        return usedClasses;
    }

    public List<SliceEntry> getAlternatives() {
        if (alternatives == null) {
            if (classfile.getAlternatives() == null) {
                alternatives = Collections.emptyList();
            } else {
                alternatives = new ArrayList<SliceEntry>(classfile.getAlternatives().size());
                for (ClassFileEntry cf : classfile.getAlternatives()) {
                    usedClasses.add(context.uniqueEntry(cf));
                }
            }
        }
        return alternatives;
    }
}
