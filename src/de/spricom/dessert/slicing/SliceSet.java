package de.spricom.dessert.slicing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import de.spricom.dessert.resolve.ClassContainer;
import de.spricom.dessert.resolve.ClassPackage;

/**
 * A SliceSet is as Set of the slices for which the elements of each {@link Slice} have common properties.
 * I. e. they belong to the same parent package, the same root, implement the same interface, comply with
 * the same naming convention etc.  
 */
public class SliceSet implements Iterable<Slice> {
    private List<Slice> slices = new ArrayList<>();

    @Override
    public Iterator<Slice> iterator() {
        return slices.iterator();
    }

    public SliceSet slice(Predicate<SliceEntry> predicate) {
        SliceSet ss = new SliceSet();
        
        return ss;
    }
    
    void add(ClassContainer cc, SliceContext context) {
        if (cc == null || cc.getClasses().isEmpty()) {
            return;
        }
        slices.add(new Slice(cc, context));
    }
    
    void addRecursive(ClassContainer cc, SliceContext context) {
        add(cc, context);
        for (ClassPackage subp : cc.getSubPackages()) {
            addRecursive(subp, context);
        }
    }
}
