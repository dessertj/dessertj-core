package de.spricom.dessert.assertions;

import de.spricom.dessert.groups.PartSlice;
import de.spricom.dessert.util.DependencyGraph;

public interface CycleRenderer<S extends PartSlice> {
    String renderCycle(DependencyGraph<S> dag);
}
