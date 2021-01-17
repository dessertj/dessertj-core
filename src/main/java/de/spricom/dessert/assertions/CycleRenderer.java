package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.util.DependencyGraph;

public interface CycleRenderer {
    String renderCycle(DependencyGraph<Slice> dag);
}
