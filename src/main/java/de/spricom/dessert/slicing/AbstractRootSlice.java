package de.spricom.dessert.slicing;

public abstract class AbstractRootSlice extends AbstractSlice {

    public Slice packageOf(Class<?> clazz) {
        return packageOf(clazz.getPackage());
    }

    public Slice packageOf(Package pkg) {
        return packageOf(pkg.getName());
    }

    public Slice packageOf(String packageName) {
        return slice(packageName + ".*");
    }

    public Slice packageTreeOf(Class<?> clazz) {
        return packageTreeOf(clazz.getPackage());
    }

    public Slice packageTreeOf(Package pkg) {
        return packageTreeOf(pkg.getName());
    }

    public Slice packageTreeOf(String packageName) {
        return slice(packageName + "..*");
    }
}
