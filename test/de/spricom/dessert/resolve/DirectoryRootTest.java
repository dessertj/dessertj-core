package de.spricom.dessert.resolve;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import de.spricom.dessert.resolve.ClassContainer;
import de.spricom.dessert.resolve.DirectoryRoot;

public class DirectoryRootTest {
   
    private DirectoryRoot cr;
    
    @Before
    public void init() throws URISyntaxException  {
        File root = new File(this.getClass().getResource("/").toURI());
        cr = new DirectoryRoot(root);
    }
    
    @Test
    public void testExisting() {
        String packagename = this.getClass().getPackage().getName();
        assertThat(cr.resolve(packagename)).isTrue();
        ClassContainer cc = cr;
        for (String segment : packagename.split("\\.")) {
            cc = cc.find(segment);
            assertThat(cc).as(segment).isNotNull();
        }
        assertThat(cr.resolve("nonsense")).isTrue();
    }

    @Test
    public void testNotExisting() {
        String packagename = this.getClass().getPackage().getName() + ".blub";
        assertThat(cr.resolve(packagename)).isFalse();
        assertThat(cr.resolve("nonsense")).isFalse();
        assertThat(cr.getFirstChild()).isNull();
    }
}
