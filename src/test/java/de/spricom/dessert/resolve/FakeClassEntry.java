package de.spricom.dessert.resolve;

import de.spricom.dessert.classfile.ClassFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class FakeClassEntry extends ClassEntry {

    FakeClassEntry(String classname, ClassPackage pckg) {
        super(classname, pckg);
    }

    @Override
    protected ClassFile resolveClassFile() {
        try {
            return new ClassFile(this.getClass());
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to resolve " + FakeClassEntry.class.getSimpleName(), ex);
        }
    }

    @Override
    public URI getURI() {
        try {
            return new URI("fake:" + getPackage().getRootFile().getPath().replace(File.separatorChar, '/') + "/" + getClassname());
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Invalid URI for " + getClassname(), ex);
        }
    }
}
