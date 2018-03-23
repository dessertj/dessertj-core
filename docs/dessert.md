# Dessert

- The name comes from **DE**pendency a**SSERT**
- It's about dependencies between classes 
- A class X depends on an other class Y if X uses Y,
  hence
  - X imports Y,
  - X uses full qualified classname of Y or
  - X and Y are in the same package

--

## In this context a 'class' is

is everthing represented by a separate class rootFile:

- traditional java class
- interface
- annotation
- any type of inner class
- enum

--

## Class X uses class Y means

- X extends or implements Y
- X has a field, method parameter or local variable of type Y
- X references a static method of Y
- X references method of Y (direct call or λ)
- X throws Y
- X uses generic type of Y

--

## Special case 

- A static inner class does not depend on it's surrounding class
- Dessert tries to produce the same results as jDeps

---

# Related Products

--

---

# Using jDeps

    jdeps -verbose:class -filter:none -cp build/classes ^
    build/classes/java/test/de/spricom/dessert/test/classfile/InnerClassesDependenciesTest* ^
    > build\jdeps.txt
