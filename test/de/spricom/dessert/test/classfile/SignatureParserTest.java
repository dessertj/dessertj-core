package de.spricom.dessert.test.classfile;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import de.spricom.dessert.classfile.attribute.SignatureParser;

public class SignatureParserTest {
	
	@Test
	public void testJavaTypeSignature() {
		checkJavaTypeSignature("B");
		checkJavaTypeSignature("Ljava/lang/String;", "java.lang.String");
		checkJavaTypeSignature("[Ljava/lang/String;", "java.lang.String");
		checkJavaTypeSignature("[[I");
		checkJavaTypeSignature("Ljava/util/Set<Ljava/lang/String;>;", "java.lang.String", "java.util.Set");
	}
	
	private void checkJavaTypeSignature(String signature, String... dependentClasses) {
		SignatureParser parser = new SignatureParser(signature);
		parser.parseJavaTypeSignature();
		assertThat(parser.isComplete()).isTrue();
	}

	@Test
	public void testMethodSignature() {
		checkMethodSignature("(Ljava/util/Set<Ljava/lang/String;>;)V", "java.lang.String", "java.util.Set");
	}
	
	private void checkMethodSignature(String signature, String... dependentClasses) {
		SignatureParser parser = new SignatureParser(signature);
		parser.parseMethodSignature();
		assertThat(parser.isComplete()).isTrue();
	}
}
