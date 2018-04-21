dessert
=======

The name is a short form of **de**pendency a**ssert**. Hence Dessert is a library to check assertions for
dependencies. It is intended to be used within unit-tests.

Features
--------

- Checking dependency rules
- Detection of cyclic dependencies

Goals
-----

- No additional dependencies but plain Java 6 or above
- Simple and intuitive API (motivated by [AssertJ](https://joel-costigliola.github.io/assertj/))
- Assertions should be robust against refactorings (no strings for class or package names required)
- Easy and seamless integration with other testing or assertion frameworks
- Speed

Getting Started
---------------

After having included the test dependency `com.github.hajo70:dessert:0.3` from the
`https://jitpack.io` repository a test checking all dependencies of the dessert library can
be implemented like this:

    @Test
    public void checkDessertDependencies() throws IOException {
        SliceContext sc = new SliceContext();
        Slice dessert = sc.packageTreeOf("de.spricom.dessert")
                .without(sc.packageTreeOf("de.spricom.dessert.test"));
        Slice java = sc.packageTreeOf("java");
        SliceAssertions.assertThat(dessert).usesOnly(java);
    }

### Minimal Maven POM

A copy of the following Maven POM can be used to get started with dessert:

    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
    
        <groupId>com.company.samples</groupId>
        <artifactId>dessert-sample</artifactId>
        <version>1.0-SNAPSHOT</version>
    
        <dependencies>
            <dependency>
                <groupId>com.github.hajo70</groupId>
                <artifactId>dessert</artifactId>
                <version>0.3</version>
                <scope>test</scope>
            </dependency>
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>4.12</version>
                <scope>test</scope>
            </dependency>
        </dependencies>
    
        <repositories>
            <repository>
                <id>jitpack.io</id>
                <url>https://jitpack.io</url>
            </repository>
        </repositories>
    </project>

### Minimal Gradle Buildfile

The corresponding gradle build file looks like this:

    apply plugin: 'java'
    
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
    
    dependencies {
        testCompile 'com.github.hajo70:dessert:0.3'
        testCompile 'junit:junit:4.12'
    }

### Samples

There is a separate [dessert-samples](https://github.com/hajo70/dessert-samples) that
shows how to use dessert with features of Java 8.

Using dessert
-------------

- goal of dependency checking is finding unwanted dependencies
- terms: building block, layer, vertical slice
- mapping of building blocks to physical layout
- dependencies to external libraries
- what to check
- operates on .class files
- limitations
- what is a .class file
- what does class X dependends on class Y mean
- elements of dessert API
- problem of cycles
- cycle detection
- MVP-slices
- exploring dependencies

Basics
======

Classes and their dependencies
------------------------------

The goal of dependency checking is finding unwanted dependencies. Dessert does this be analyzing
.class files. The java compiler generates a .class file for each

- class
- interface
- annotation
- (anyonymous) innerclass or inner interface
- enum class

*Note: A java source file can define more than one class.*

Such a .class file X depends on an other .class file Y if X uses Y, thus

- X extends or implements Y
- X has a field of type Y, 
- X uses Y in a method signature (parameter, return value, throws clause)
- X has a local variable of Y
- X uses a (static) method of Y (direct call or method reference)
- X throws Y
- X implements Y within a λ-expression
- X is an inner class of Y (or the other way round)
- X uses generic type of Y
- X is annotated with Y
- X uses Y as an annotation parameter

*Note:*
- Import statements are no relevant, because they don't appear in a .class file.
- It's not possible to detect all of these uses (i. e. local variables, method references)
  by reflection.
- Jdeps does not consider classes used in annotation parameters as a dependency, but dessert does.
- The compiler my have removed some source dependency that cannot be detected in the .class file
  anymore.

Building Blocks
---------------

In a clean software design each class belongs to some building block. Each build block has
defined interfaces and clear dependencies. In complex systems building blocks may be composed
from other building blocks or they may be organized in layers or vertical slices.

Ideally there is a clear and intuitive mapping between the physical package structure and the
building blocks of a software:

![Structual Architecture](structural-architecture.svg)

Dessert helps you to move towards such a clean software design and alarms you immediately 
if you are moving backward.

Main Elements of the Dessert API
--------------------------------

#### Slice
For dessert your software is just a bunch of classes. Hence you have to cut it down to
pieces that make up building blocks. In dessert such a piece is a called a `Slice`. 
A `Slice` is an arbitrary slice of the set of all classes that belong to a software.
A `Slice` is immutable. The method `slice` can be used to create as smaller `Slice` from
a existing `Slice`. Two slices can be combined with the methods `with` (union) and
`without` (difference) to a new `Slice`.  

#### SliceEntry
Each `SliceEntry` represents a .class file. It has methods like `getClassName()` or
`getClazz()` to access the details of the .class to be used for predicates. It's most
important method is `getUsedClasses()`. This is used by the `SliceAssertions`.

#### SliceContext
The `SliceContext` is the entry point to dessert, a factory for slices. To get your
initial slices you can use the methods `packageOf`, `packageTreeOf` or `sliceOf`. 
The `SliceContext` implements the fly-weight pattern for slice entries. Hence for
two 'SliceEntry' objects x and y that originate from the same `SliceContext`
x.equals(y) is equivalent to x == y. Of nothing else is specified the `SliceContext`
operates on the current class-path.

#### SliceAssertions
`SliceAssertions` is a utility class that provides a fluent API with static methods
analogous to [AssertJ](https://joel-costigliola.github.io/assertj/). It's most important
method is `assertThat` or it's synonym `dessert`, so that it can be used with static
imports without conflicting with AssertJ. The most important methods of the fluent API
are `doesNotUse`, `usesOnly` or the combination of `uses`, `and` and `only()`.

Groups and Cycles
-----------------

The problem with a dependency cycle is: there is no starting point. Thus you cannot use
or test a class involved in a cycle without having all other classes available. (By using
a mocking framework testing of an isolated class is possible with limitations.) Small 
cycles between classes are often necessary, but big interwined cycles make your software
a ball of wool. In such an environment testing is a nightmare, because each simple test
needs a very complex setup that initializes all parts of the software. It is not possible
to re-use such a software without replicating the whole infrastructure with all it's system
requirements, event if only a small part is required.

#### SliceGroup
For cycle detection dessert provides the concept of a `SliceGroup`. A the name says, a
`SliceGroup` is a group of `Slice` objects. The `SliceGroup` has the static convienience
factory methods `splitByPackage` and `splitByEntry` to split up a `Slice` into smaller
corresponding slices. By providing a `SlicePartioner` an arbitrary criterium can be used
to split up a `Slice`. All `SliceEntry` objects the `partKey` methods maps to the same
string will be in the same  `Slice`.

#### Cycle detection
To detect cycles for a `SliceGroup` *sg* you can use:

    SliceGroup<PackageSlice> sg = SliceGroup.splitByPackage(slice);
    SliceAssertions.dessert(sg).isCycleFree();

This can be shortend to:

    SliceAssertions.dessert(slice).splitByPackage().isCycleFree();

#### Enforcing nesting rules
One method to prevent package cycles is establishing dependency rules for nested packages. For
example dessert follows the rule a deeper nested package must not use a class from it's parent
package. `SliceGroup` implements the `Iterator` interface, hence the following code can be used
to enforce this rule:

    Slice slice = new SliceContext().packageTreeOf("de.spricom.dessert");
    SliceGroup<PackageSlice> packages = SliceGroup.splitByPackage(slice);

    packages.forEach(pckg -> SliceAssertions.assertThat(pckg)
            .doesNotUse(pckg.getParentPackage(packages)));

The code above disallows only dependencies to direct parent packages. To disallow dependencies
to any ancestor package one could write:

    Slice slice = new SliceContext().packageTreeOf("de.spricom.dessert");
    SliceGroup<PackageSlice> packages = SliceGroup.splitByPackage(slice);

    packages.forEach(pckg -> SliceAssertions.assertThat(pckg)
            .doesNotUse(slice.slice(entry -> pckg.getParentPackageName().startsWith(entry.getPackageName()))));

Duplicates
----------

A common source of errors are duplicate .class files on the class-path. To load a class only it's fully qualified
name is required. Therefore the ClassLoader scan the entries on the class-path for the first .class file with
that name. Thus the order on the class-path matters. The same .class file may appear in different libraries and
the one loaded by the ClassLoader may not be the one you want.

For dessert two `SliceEntry` objects are only equal if they point to the same .class file, thus their `getURI`
method returns the same value. The ``getAlternatives()`` method returns all `SliceEntry` objects found by dessert
that have the same fully qualified classname. Ideally there is only one alternative - the `SliceEntry` itself.

By default the `SliceContext` scans the whole class-path and finds all duplicates (to be precise it scans all
jar's and class-directories visible to it's `Resolver`). The `duplicates` method returns a `Slice` containing
all duplicates. Hence the following code can be used to ensure there are none:   

    ConcreteSlice duplicates = new SliceContext().duplicates();
    StringBuilder sb = new StringBuilder();
    duplicates.getSliceEntries().forEach(entry -> sb.append(entry.getURI()).append("\n"));
    assertThat(duplicates.getSliceEntries()).as(sb.toString()).isEmpty();

Usage
=====

tbd.    