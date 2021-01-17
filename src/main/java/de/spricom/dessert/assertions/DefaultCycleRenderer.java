package de.spricom.dessert.assertions;

import de.spricom.dessert.slicing.Slice;
import de.spricom.dessert.util.DependencyGraph;

public class DefaultCycleRenderer implements CycleRenderer {

    @Override
    public String renderCycle(DependencyGraph<Slice> dag) {
        StringBuilder sb = new StringBuilder("Cycle:\n");
        int count = 0;
        for (Slice n : dag.getCycle()) {
            sb.append(count == 0 ? "" : ",\n");
            sb.append(n.toString());
            count++;
        }
        return sb.toString();
    }
}
