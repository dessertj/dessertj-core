package de.spricom.dessert.partitioning;

import de.spricom.dessert.slicing.SlicePartitioner;

import static de.spricom.dessert.partitioning.ClazzPredicates.*;

public class SlicePartitioners {

    public static final SlicePartitioner INTERFACES = new SlicePartitionerBuilder()
            .split("interfaces, enums an annotations")
            .by(or(INTERFACE, ENUM, ANNOTATION))
            .split("anything else")
            .by(or(EACH))
            .build();
}
