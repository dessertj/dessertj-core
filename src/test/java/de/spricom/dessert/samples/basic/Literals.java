package de.spricom.dessert.samples.basic;

public class Literals {

    private Class<?> fooclass = Foo.class;

    public void go() {
        System.out.println(Bar.class.getName());
        Class<?> baz = Baz.class;
        System.out.println(baz.getName());
    }
}
