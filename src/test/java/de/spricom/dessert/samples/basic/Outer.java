package de.spricom.dessert.samples.basic;

public class Outer {

    public interface InnerIfc {
        String CONST = "constant";

        String someMethod();
    }

    private class Inner implements InnerIfc {

        @Override
        public String someMethod() {
            return "done";
        }
    }

    private InnerIfc usage = new Inner();

    public InnerIfc useAnonymous() {
        final String value = "value";

        return new InnerIfc() {
            @Override
            public String someMethod() {
                return value;
            }
        };
    }
}
