package de.spricom.dessert.test.classfile;

import de.spricom.dessert.classfile.ClassFile;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class SpecialClassesDependenciesTest {

    @Test
    public void testAnnotation() throws IOException {
        ClassFile cf = new ClassFile(com.sun.javafx.beans.IDProperty.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "java.lang.Object",
                "java.lang.String",
                "java.lang.annotation.Annotation",
                "java.lang.annotation.Documented",
                "java.lang.annotation.Inherited",
                "java.lang.annotation.Retention",
                "java.lang.annotation.Target"
        );
    }

    @Test
    public void testKeyFrame() throws IOException {
        ClassFile cf = new ClassFile(javafx.animation.KeyFrame.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "java.lang.AssertionError",
                "java.lang.Class",
                "java.lang.IllegalArgumentException",
                "java.lang.NullPointerException",
                "java.lang.Object",
                "java.lang.String",
                "java.lang.StringBuilder",
                "java.util.Collection",
                "java.util.Collections",
                "java.util.Iterator",
                "java.util.Set",
                "java.util.concurrent.CopyOnWriteArraySet",
                "javafx.animation.KeyValue",
                "javafx.beans.NamedArg",
                "javafx.event.ActionEvent",
                "javafx.event.EventHandler",
                "javafx.util.Duration"
        );
    }

    @Test
    public void testMappingChange() throws IOException {
        ClassFile cf = new ClassFile(com.sun.javafx.collections.MappingChange.class);
        assertThat(cf.getDependentClasses()).containsOnly(
                "com.sun.javafx.collections.ChangeHelper",
                "com.sun.javafx.collections.MappingChange$1",
                "com.sun.javafx.collections.MappingChange$2",
                "com.sun.javafx.collections.MappingChange$Map",
                "java.lang.String",
                "java.lang.StringBuilder",
                "java.util.List",
                "javafx.collections.ListChangeListener",
                "javafx.collections.ListChangeListener$Change",
                "javafx.collections.ObservableList"
        );
    }

    @Test
    public void testBehaviorBase() throws IOException {
        ClassFile cf = new ClassFile(com.sun.javafx.scene.control.behavior.BehaviorBase.class);
        // 144: methodType: "(Ljavafx/event/Event;)V"
        System.out.println(com.sun.javafx.scene.control.behavior.BehaviorBase.class.getResource("BehaviorBase.class"));
        System.out.println(cf.dumpConstantPool());
        assertThat(cf.getDependentClasses()).containsOnly(
                "com.sun.javafx.scene.control.behavior.KeyBinding",
                "com.sun.javafx.scene.traversal.Direction",
                "java.lang.NullPointerException",
                "java.lang.Object",
                "java.lang.String",
                "java.lang.invoke.CallSite",
                "java.lang.invoke.LambdaMetafactory",
                "java.lang.invoke.MethodHandle",
                "java.lang.invoke.MethodHandles",
                "java.lang.invoke.MethodHandles$Lookup",
                "java.lang.invoke.MethodType",
                "java.util.ArrayList",
                "java.util.Collection",
                "java.util.Collections",
                "java.util.List",
                "javafx.application.ConditionalFeature",
                "javafx.application.Platform",
                "javafx.beans.InvalidationListener",
                "javafx.beans.Observable",
                "javafx.beans.property.ReadOnlyBooleanProperty",
                "javafx.event.EventHandler",
                "javafx.event.EventType",
                "javafx.scene.Node",
                "javafx.scene.control.Control",
                "javafx.scene.input.ContextMenuEvent",
                "javafx.scene.input.KeyCode",
                "javafx.scene.input.KeyEvent",
                "javafx.scene.input.MouseEvent"
        );
    }
}
