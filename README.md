Dessert
=======

The name is a short form of **De**pendency A**ssert**. Hence Dessert is a library to check assertions for
dependencies. Typically it is used within unit-tests.

Goals
-----

- No additional dependencies but plain Java 6 or above (version 0.2 requires Java 8)
- Simple and intuitive API
- Assertions should be robust against refactorings (no strings for class or package names)
- Easy and seamless integration with other testing or assertion frameworks
- Speed

Background
----------

The aim of checking the dependencies of a class is to find any unwanted dependency. Hence for each
class there is a set of classes for which dependencies are permitted and an other set of classes
for which dependencies are unwanted or disallowed.

Typically the same dependency rules that apply to one class apply to other somehow related classes.
Thus when specifying dependencies there is a set of related classes for which there may be dependencies
to some other set of classes.

For example the main classes of the dessert project are related, because they are located in the same
source directory and they all belong to the package `de.spricom.dessert`. The set of all these classes
may have dependencies to the set of JDK classes of package `java` but they must not have dependencies
to the JDK classes of package `sun`, because those are considered internal.

A library for dependency checking needs some concept to specify a set of classes. Therefore we need to
know what a class is: Physically a class is .class file located in some directory tree or a .jar file.
Within a directory tree a class is uniquely defined by its name and its position in the tree structure.
This can be expressed by the fully qualified class name (fqcn), 
i.e. `de.spricom.dessert.classfile.constpool.ConstantPool`. The same applies to a JAR file. But a class
with the same fqcn can appear in different directories or JAR files. Thus we need
besides the fqcn always it's container (directory or JAR file) to specify it
uniquely.

For the following a container is directory or JAR file that could be added to the CLASSPATH to include
all classes within the container. A class is a concrete .class file uniquely defined by its fqcn name
and its container. Hence for the concepts below an interface or an inner class is a class because
it has its own .class file.

To specify a set of classes we need the fqcns and the containers. It is obvious to use patterns to specify
the class names. For example all classes of the `de.spricom.dessert.classfile` package and it's sub-packages
could by a set of classes or all `*Impl` classes of any `view` package could by an other set. 

Selecting classes by name is often not sufficient for dependency checking. For example only the public 
interfaces of some package may be allowed to be used. Thus besides the container and patterns for the fqcn
we need be able to specify additional selection criteria to define a set of classes for dependency checking.

In Dessert a set of classes is represented by a `SliceSet`. All classes that belong to a `SliceSet` comply 
with the same selection criteria. A `SliceSet` consists of `Slice` objects. Each `Slice` represents the
classes of one package inside one container that comply with the selection criteria of a `SliceSet`. Hence
each `Slice` has a package name and a container. The same `Slice` object never belongs to two different
`SliceSet` objects.

The classes belonging to a `Slice` are represented by `SliceEntry` objects. Each `SliceEntry` corresponds
with a class file inside a container. The same `SliceEntry` object may belong to different `Slice` objects if
it fulfills all the corresponding selection criteria. The `SliceEntry` provides an API to check selection
criteria and to access all direct dependencies of a class.

The starting point for any dependency analysis with Dessert is the `SliceContext`. The `SliceContext` implements
the flyweight pattern for `SliceEntry` objects. Thus for two `SliceEntry` objects `se1` and `se2`
`se1.equals(se2)` is equivalent to `se1 == se2` if they come from the same `SliceContext`. Thus checking whether
some dependency belongs to a `SliceSet` is very fast. For performance reasons all dependency tests should use
the same `SliceContext`.

The `SliceContext` provides some methods (`packagesOf`, `subPackagesOf`) to create an initial `SliceSet`
whos slices contain all classes. The `slice` method of the intial `SliceSet` can be used with a corresponding
`ClassPredicate` to create smaller `SliceSet` objects. For the actual dependency checking between such
`SliceSet` objects the `SliceAssertions` class provides a fluent API.  

Thus a simple test could look like this:

    @Test
    public void testExternalDependencies() throws IOException {
        SliceContext sc = new SliceContext();
        SliceSet dessert = sc.subPackagesOf("de.spricom.dessert")
                .without(sc.subPackagesOf("de.spricom.dessert.test"));
        SliceSet java = sc.subPackagesOf("java.lang")
                .with(sc.subPackagesOf("java.util"))
                .with(sc.subPackagesOf("java.io"))
                .with(sc.subPackagesOf("java.net"))
                .with(sc.subPackagesOf("java.security"));
        SliceAssertions.assertThat(dessert).usesOnly(java);
    }

DuplicateClassFinder
====================

The DuplicateClassFinder is included in Dessert. It checks if there are different implementations of
the same class on the class-path. You can use it form a Gradle file like that:

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

