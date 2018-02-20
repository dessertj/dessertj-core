package de.spricom.dessert.test.classfile.samples;

public class SpecialArgSample {

    public void doSomething(@SpecialArg int arg) {
        System.out.println("arg: " + arg);
    }
}
