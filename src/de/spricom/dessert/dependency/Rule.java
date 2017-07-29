package de.spricom.dessert.dependency;

@FunctionalInterface
public interface Rule {
    boolean isMember(ClassFileEntry entry);
}
