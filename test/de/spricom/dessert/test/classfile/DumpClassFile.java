package de.spricom.dessert.test.classfile;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;
import org.springframework.aop.framework.autoproxy.BeanFactoryAdvisorRetrievalHelper;
import org.springframework.aop.target.dynamic.BeanFactoryRefreshableTargetSource;
import org.springframework.context.ApplicationContext;

import de.spricom.dessert.classfile.ClassFile;
import de.spricom.dessert.classfile.FieldInfo;
import de.spricom.dessert.classfile.MethodInfo;
import de.spricom.dessert.classfile.attribute.Annotation;
import de.spricom.dessert.traversal.ClassVisitor;
import de.spricom.dessert.traversal.PathProcessor;

public class DumpClassFile {

	@Test
	public void testMe() throws IOException {
		dump(getClass());
	}

	@Test
	public void testClassfile() throws IOException {
		dump(ClassFile.class);
	}

	@Test
	public void testPathProcessor() throws IOException {
		dump(PathProcessor.class);
	}

	@Test
	public void testClassVisitor() throws IOException {
		dump(ClassVisitor.class);
	}

	@Test
	public void testTestAnnotation() throws IOException {
		dump(Test.class);
	}

	@Test
	public void testApplicationContext() throws IOException {
		dump(ApplicationContext.class);
	}

	@Test
	public void testSpringApoFactory() throws IOException {
		dump(BeanFactoryRefreshableTargetSource.class);
	}

	@Test
	public void testSpringAopHelper() throws IOException {
		dump(BeanFactoryAdvisorRetrievalHelper.class);
	}

	@Test
	public void test594Dependencies() throws IOException {
		dump(com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration.class);
	}

	@Test
	public void testAnnotation() throws IOException {
		dump(Annotation.class);
	}

	@Test
	public void testArrayList() throws IOException {
		dump(ArrayList.class);
	}
	/*
junit-4.12.jar -> C:\Program Files\Java\jdk1.8.0_40\jre\lib\rt.jar
   org.junit.rules.TestWatchman$1 (junit-4.12.jar)
      -> java.lang.Object                                   
      -> java.lang.Throwable                                
      -> org.junit.internal.AssumptionViolatedException     junit-4.12.jar
      -> org.junit.runners.model.FrameworkMethod            junit-4.12.jar
      -> org.junit.runners.model.Statement                  junit-4.12.jar
org.junit.rules.TestWatchman$1: [java.lang.Object]
	 */
	@Test
	public void testTestWatchman() throws IOException {
		System.out.println(org.junit.rules.TestWatchman.class.getResource("TestWatchman$1.class"));
		dump(new ClassFile(org.junit.rules.TestWatchman.class.getResourceAsStream("TestWatchman$1.class")));
	}

	private void dump(Class<?> clazz) throws IOException {
		ClassFile cf = new ClassFile(clazz);
		Set<String> dependentClasses = dump(cf);
		assertThat(dependentClasses).excludes(clazz.getName());
		if (clazz.getSuperclass() != null) {
			assertThat(dependentClasses).contains(clazz.getSuperclass().getName());
		}
	}

	private Set<String> dump(ClassFile cf) {
		System.out.println(cf.getThisClass());
		System.out.println("extends " + cf.getSuperClass());
		System.out.println("implements " + Arrays.toString(cf.getInterfaces()));
		System.out.println("constant pool:");
		System.out.println(cf.dumpConstantPool());
		System.out.println("fields:");
		for (FieldInfo field : cf.getFields()) {
			System.out.print("  ");
			System.out.println(field.getDeclaration());
		}
		System.out.println("methods:");
		for (MethodInfo method : cf.getMethods()) {
			System.out.print("  ");
			System.out.println(method.getDeclaration());
		}
		System.out.println("depends:");
		Set<String> dependentClasses = cf.getDependentClasses();
		dependentClasses.forEach(c -> System.out.println("  " + c));
		return dependentClasses;
	}
}
