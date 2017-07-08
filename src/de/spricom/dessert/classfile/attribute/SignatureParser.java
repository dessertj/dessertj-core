package de.spricom.dessert.classfile.attribute;

import java.util.HashSet;
import java.util.Set;

public class SignatureParser {
	private Set<String> dependentClasses;
	private String signature;
	private int position;
	
	public SignatureParser(String signature, Set<String> dependentClasses) {
		this.dependentClasses = dependentClasses;
		this.signature = signature;
	}

	public SignatureParser(String signature) {
		this(signature, new HashSet<>());
	}
	
	public boolean parseClassSignature() {
		return false;
	}
	
	
	public boolean parseJavaTypeSignature() {
		return parseBaseType() 
				|| parseReferenceTypeSignature();
	}

	private boolean parseReferenceTypeSignature() {
		return parseClassTypeSignature()
				|| parseTypeVariableSignature()
				|| parseArrayTypeSignature();
	}

	private boolean parseClassTypeSignature() {
		if ('L' != lookAhead()) {
			return false;
		}
		position++;
		parsePackageSpecifier();
		ensure(parseSimpleClassTypeSignature());
		while (parseClassTypeSignatureSuffix());
		ensure(';' == lookAhead());
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
		while(parseTypeArgument());
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
		return signature.charAt(position);
	}
	
	private void ensure(boolean parsed) {
		if (!parsed) {
			throw new IllegalArgumentException(signature + " is invalid at position " + position + ": " + signature.substring(position));
		}
	}
	
	public boolean isComplete() {
		return position == signature.length();
	}
}
