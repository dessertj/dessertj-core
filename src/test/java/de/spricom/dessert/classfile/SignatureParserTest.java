package de.spricom.dessert.classfile;

import de.spricom.dessert.classfile.attribute.SignatureParser;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test the {@link SignatureParser}.
 */
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
		assertThat(parser.getDependentClasses()).contains((Object[])dependentClasses);
	}

	@Test
	public void testMethodSignature() {
		checkMethodSignature("(Ljava/util/Set<Ljava/lang/String;>;)V", "java.lang.String", "java.util.Set");
		checkMethodSignature("<T:Ljava/lang/Object;>([TT;)[TT;", "java.lang.Object");
	}
	
	private void checkMethodSignature(String signature, String... dependentClasses) {
		SignatureParser parser = new SignatureParser(signature);
		parser.parseMethodSignature();
		assertThat(parser.isComplete()).isTrue();
		assertThat(parser.getDependentClasses()).contains((Object[])dependentClasses);
	}
	
	@Test
	public void testClassSignature() {
		checkClassSignature("Ljava/lang/Enum<Lde/spricom/dessert/classfile/attribute/AttributeInfo$AttributeContext;>;", "java.lang.Enum", "de.spricom.dessert.classfile.attribute.AttributeInfo$AttributeContext");
		checkClassSignature("<A:Ljava/lang/Object;>Ljava/lang/Object;", "java.lang.Object");
	}
	
	private void checkClassSignature(String signature, String... dependentClasses) {
		SignatureParser parser = new SignatureParser(signature);
		parser.parseClassSignature();
		assertThat(parser.isComplete()).isTrue();
		assertThat(parser.getDependentClasses()).contains((Object[])dependentClasses);
	}
}
