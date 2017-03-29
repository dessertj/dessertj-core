# Dessert

The name is a short form of _De_pendency A_ssert_. Hence Dessert is a library to check assertions for
dependencies. Typically it is used for unit-tests.

## Goals

- No additional dependencies but plain Java 8
- Simple and intuitive API
- Assertions should be robust against refactorings (no strings for class or package names)
- Easy and seamless integration with other testing or assertion frameworks
- Speed

## Progress

# DuplicateClassFinder

The DuplicateClassFinder is included in Dessert. It checks if there are different implementations of
the same class on the class-path. You can use it like that:

	apply plugin: 'java'
	
	repositories {
	    jcenter()
	    maven { url 'https://jitpack.io' }
	}
	
	configurations {
		dessert
	}
	
	dependencies {
		dessert 'com.github.hajo70:dessert:0.1'
		
		runtime 'org.apache.httpcomponents:httpclient:4.5.3'
		runtime 'org.keycloak:keycloak-osgi-thirdparty:1.1.1.Final'
	}
	
	task findDuplicates(type: JavaExec) {
	  classpath = files(configurations.dessert, configurations.runtime)
	  main = 'de.spricom.dessert.duplicates.DuplicateClassFinder'
	}

