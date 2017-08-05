package de.spricom.dessert.test.dependency;

import java.io.IOException;

import org.fest.assertions.Assertions;
import org.junit.Test;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.dependency.ClassFileEntry;
import de.spricom.dessert.dependency.DependencyUnit;
import de.spricom.dessert.dependency.Repository;
import de.spricom.dessert.traversal.PathProcessor;

public class RepositoryTest {

    @Test
    public void test() throws IOException {
        PathProcessor proc = new PathProcessor();
        proc.setPath("bin");
        Repository repository = new Repository();
        proc.traverseAllClasses(repository);
        
        ClassFileEntry entry = repository.lookup(ClassFile.class.getName());
        Assertions.assertThat(entry).isNotNull();
        
        DependencyUnit unit = repository.getDependencyUnit(this::rule);
        System.out.println(unit.getMembers().keySet());
        Assertions.assertThat(unit.getMembers().containsValue(entry)).isTrue();
    }
    
    private boolean rule(ClassFileEntry entry) {
        System.out.println("Checking " + entry.getClassName());
        return entry.getClassName().startsWith(ClassFile.class.getPackage().getName());
    }
}
