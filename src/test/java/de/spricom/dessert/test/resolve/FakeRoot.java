package de.spricom.dessert.test.resolve;

import de.spricom.dessert.resolve.ClassContainer;
import de.spricom.dessert.resolve.ClassFileEntry;
import de.spricom.dessert.resolve.ClassResolver;
import de.spricom.dessert.resolve.ClassRoot;

import java.io.File;
import java.util.LinkedList;

public class FakeRoot extends ClassRoot {
    FakeRoot(ClassResolver resolver, File file) {
        super(resolver, file);
    }

    public void add(String classname) {
        int index = classname.lastIndexOf('.');
        if (index == -1) {
            addClass(this, classname);
        } else {
            addClass(addPackage(classname.substring(0, index)), classname);
        }
    }

    private void addClass(ClassContainer cc, String classname) {
        if (cc.getClasses() == null) {
            cc.setClasses(new LinkedList<ClassFileEntry>());
        }
        FakeClassFileEntry cfe = new FakeClassFileEntry(classname, cc);
        addClass(cfe);
        cc.getClasses().add(cfe);
    }
}
