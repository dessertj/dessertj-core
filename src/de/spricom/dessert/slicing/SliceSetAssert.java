package de.spricom.dessert.slicing;

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
        dag.getSorted();
    }
}
