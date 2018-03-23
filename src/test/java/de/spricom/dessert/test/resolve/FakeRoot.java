package de.spricom.dessert.test.resolve;

import de.spricom.dessert.resolve.*;

import java.io.File;
import java.util.LinkedList;

public class FakeRoot extends ClassRoot {
    FakeRoot(ClassResolver resolver, File file) {
        super(resolver, file);
    }

    @Override
    protected void scan(ClassCollector classCollector) {

    }

    public void add(String classname) {
        int index = classname.lastIndexOf('.');
        if (index == -1) {
            addClass(this, classname);
        } else {
            addClass(addPackage(classname.substring(0, index)), classname);
        }
    }

    private void addClass(ClassPackage cc, String classname) {
        if (cc.getClasses() == null) {
            cc.setClasses(new LinkedList<ClassEntry>());
        }
        FakeClassEntry cfe = new FakeClassEntry(classname, cc);
        addClass(cfe);
        cc.getClasses().add(cfe);
    }
}
