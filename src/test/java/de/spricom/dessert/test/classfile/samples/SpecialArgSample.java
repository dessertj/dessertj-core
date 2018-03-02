package de.spricom.dessert.test.classfile.samples;

/**
 * This class uses a parameter annotation. The dependency to that annotation must be detected.
 */
public class SpecialArgSample {

    public void doSomething(@SpecialArg int arg) {
        System.out.println("arg: " + arg);
    }
}
