package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.Classpath;
import de.spricom.dessert.slicing.Clazz;
import org.junit.Test;

import static de.spricom.dessert.assertions.SliceAssertions.dessert;
import static org.fest.assertions.Assertions.assertThat;

public class IllegalDependenciesTest {

    @Test
    public void testDetectOneIllegalDependency() {
        Classpath cp = new Classpath();
        Clazz me = cp.asClazz(this.getClass());
        try {
            dessert(me).usesNot(cp.rootOf(Test.class));
        } catch (AssertionError er) {
            assertThat(er.getMessage()).isEqualTo("Illegal Dependencies:\n" +
                    "de.spricom.dessert.assertions.IllegalDependenciesTest\n" +
                    " -> org.junit.Test\n");
        }
    }
}
