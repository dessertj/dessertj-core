package de.spricom.dessert.test.samples;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.slicing.Dessert;
import de.spricom.dessert.slicing.SliceSet;

public class DependencyCheck {

    @Test
    public void testPackageDependencies() {
        Package pkg = Package.getPackage("de.spricom.dessert");
        assertThat(pkg).isNotNull();
       
        SliceSet subPackages = Dessert.subPackagesOf(pkg);
        Dessert.dessert(subPackages).areCycleFree();
        
        subPackages.forEach(slice -> Dessert.assertThat(slice).doesNotDependOn(slice.getParentPackage()));
    }
    
    @Test
    public void checkPackageApi() {
        SortedSet<String> set = new TreeSet<>();
        for (Package p : Package.getPackages()) {
            set.add(p.getName());
        }
        set.forEach(s -> System.out.println(s));
    }
}
