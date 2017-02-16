# Dessert

The name is a short form of _De_pendency A_ssert_. Hence Dessert is a library to check assertions for
dependencies. Typically it is used for unit-test.

## Goals

- No additional dependencies but plain Java 8.
- Simple and intuitive API.
- Tests should be robust against refactorings (no strings for class or package names)
- Speed.

## Progress

# DuplicateClassFinder

The DuplicateClassFinder is included in Dessert. It checks if there are different implementations of
the same class on the class-path.
