package de.spricom.dessert.samples.annotations;

/**
 * This class uses a parameter annotation. The dependency to that annotation must be detected.
 */
public class SpecialArgSample {

    public void doSomething(@SpecialArg int arg) {
        System.out.println("arg: " + arg);
    }
}
