# Dessert

The name is a short form of _De_pendency A_ssert_. Hence Dessert is a library to check assertions for
dependencies. Typically it is used in unit-tests.

## Goals

- No additional dependencies but plain Java 8.
- Simple and intuitive API.
- Assertions should be robust against refactorings (no strings for class or package names)
- Easy and seamlessly integratable with other testing or assertion frameworks.
- Speed.

## Progress

# DuplicateClassFinder

The DuplicateClassFinder is included in Dessert. It checks if there are different implementations of
the same class on the class-path.
