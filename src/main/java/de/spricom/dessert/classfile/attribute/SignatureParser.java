package de.spricom.dessert.classfile.attribute;

import java.util.HashSet;
import java.util.Set;

public class SignatureParser {
	private final Set<String> dependentClasses;
	private final String signature;
	private int position;

	public SignatureParser(String signature, Set<String> dependentClasses) {
		this.dependentClasses = dependentClasses;
		this.signature = signature;
	}

	public SignatureParser(String signature) {
		this(signature, new HashSet<String>());
	}

	public boolean parseClassSignature() {
		parseTypeParameters();
		ensure(parseSuperclassSignature());
		while (parseSuperInterfaceSignature());
		return true;
	}

	private boolean parseTypeParameters() {
		if ('<' != lookAhead()) {
			return false;
		}
		position++;
		parseTypeParameter();
		while (parseTypeParameter())
			;
		ensure('>' == lookAhead());
		position++;
		return true;
	}

	private boolean parseTypeParameter() {
		int lastIndex = position;
		parseIdentifier();
		if (!parseClassBound()) {
			position = lastIndex;
			return false;
		}
		while (parseInterfaceBound())
			;
		return true;
	}

	private boolean parseClassBound() {
		if (':' != lookAhead()) {
			return false;
		}
		position++;
		parseReferenceTypeSignature();
		return true;
	}

	private boolean parseInterfaceBound() {
		if (':' != lookAhead()) {
			return false;
		}
		position++;
		ensure(parseReferenceTypeSignature());
		return true;
	}

	private boolean parseSuperclassSignature() {
		ensure(parseClassTypeSignature());
		return true;
	}

	private boolean parseSuperInterfaceSignature() {
		return parseClassTypeSignature();
	}

	public boolean parseMethodSignature() {
		parseTypeParameters();
		ensure('(' == lookAhead());
		position++;
		while (parseJavaTypeSignature())
			;
		ensure(')' == lookAhead());
		position++;
		ensure(parseResult());
		while (parseThrowsSignature())
			;
		return true;
	}

	private boolean parseResult() {
		return parseVoidDescriptor() || parseJavaTypeSignature();
	}

	private boolean parseVoidDescriptor() {
		if ('V' != lookAhead()) {
			return false;
		}
		position++;
		return true;
	}

	private boolean parseThrowsSignature() {
		if ('^' != lookAhead()) {
			return false;
		}
		position++;
		ensure(parseClassTypeSignature() || parseTypeVariableSignature());
		return true;
	}

	public boolean parseFieldSignature() {
		ensure(parseReferenceTypeSignature());
		return true;
	}

	public boolean parseJavaTypeSignature() {
		return parseBaseType() || parseReferenceTypeSignature();
	}

	private boolean parseReferenceTypeSignature() {
		return parseClassTypeSignature() || parseTypeVariableSignature() || parseArrayTypeSignature();
	}

	private boolean parseClassTypeSignature() {
		if ('L' != lookAhead()) {
			return false;
		}
		position++;
		int start = position;
		parsePackageSpecifier();
		ensure(parseSimpleClassTypeSignature());
		while (parseClassTypeSignatureSuffix())
			;
		ensure(';' == lookAhead());
		
		String classname = signature.substring(start, position);
		int typeIndex = classname.indexOf('<');
		if (typeIndex == -1) typeIndex = classname.length();
		dependentClasses.add(classname.substring(0, typeIndex).replace('/', '.'));
		
		position++;
		return true;
	}

	private boolean parsePackageSpecifier() {
		int lastIndex = position;
		parseIdentifier();
		if ('/' != lookAhead()) {
			position = lastIndex;
			return false;
		}
		position++;
		parsePackageSpecifier();
		return true;
	}

	private boolean parseSimpleClassTypeSignature() {
		ensure(parseIdentifier());
		parseTypeArguments();
		return true;
	}

	private boolean parseTypeArguments() {
		if ('<' != lookAhead()) {
			return false;
		}
		position++;
		ensure(parseTypeArgument());
		while (parseTypeArgument())
			;
		ensure('>' == lookAhead());
		position++;
		return true;
	}

	private boolean parseTypeArgument() {
		if ('*' == lookAhead()) {
			position++;
			return true;
		}
		parseWildcardIndicator();
		return parseReferenceTypeSignature();
	}

	private boolean parseWildcardIndicator() {
		if ("+-".indexOf(lookAhead()) == -1) {
			return false;
		}
		position++;
		return true;
	}

	private boolean parseClassTypeSignatureSuffix() {
		if ('.' != lookAhead()) {
			return false;
		}
		position++;
		ensure(parseSimpleClassTypeSignature());
		return true;
	}

	private boolean parseTypeVariableSignature() {
		if ('T' != lookAhead()) {
			return false;
		}
		position++;
		ensure(parseIdentifier());
		ensure(';' == lookAhead());
		position++;
		return true;
	}

	private boolean parseArrayTypeSignature() {
		if ('[' != lookAhead()) {
			return false;
		}
		position++;
		ensure(parseJavaTypeSignature());
		return true;
	}

	private boolean parseIdentifier() {
		if (!Character.isJavaIdentifierStart(lookAhead())) {
			return false;
		}
		position++;
		while (Character.isJavaIdentifierPart(lookAhead())) {
			position++;
		}
		return true;
	}

	private boolean parseBaseType() {
		if ("BCDFIJSZ".indexOf(lookAhead()) == -1) {
			return false;
		}
		position++;
		return true;
	}

	private char lookAhead() {
		if (position >= signature.length()) {
			return 0;
		}
		return signature.charAt(position);
	}

	private void ensure(boolean parsed) {
		if (!parsed) {
			throw new IllegalArgumentException(
					signature + " is invalid at position " + position + ": " + signature.substring(position));
		}
	}

	public boolean isComplete() {
		return position == signature.length();
	}

	public Set<String> getDependentClasses() {
		return dependentClasses;
	}

	public String getSignature() {
		return signature;
	}
}
