package de.spricom.dessert.slicing;

import de.spricom.dessert.util.DependencyGraph;
import junit.framework.AssertionFailedError;

public class SliceSetAssert {
    private final SliceSet set;
    
    public SliceSetAssert(SliceSet set) {
        this.set = set;
    }

    public void isCycleFree() {
        DependencyGraph<Slice> dag = new DependencyGraph<>();
        for (Slice n : set) {
            for (Slice m : set) {
                if (n != m && n.isUsing(m)) {
                    dag.addDependency(n, m);
                }
            }
        }
        if (!dag.isCycleFree()) {
            StringBuilder sb = new StringBuilder("Cycle:\n");
            int count = 0;
            for (Slice n : dag.getCycle()) {
                sb.append(count == 0 ? "" : ",\n");
                sb.append(n.getPackageName());
                count++;
            }
            throw new AssertionFailedError(sb.toString());
        }
    }
}
