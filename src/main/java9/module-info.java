module org.dessertj.core {
    requires java.logging;

    exports org.dessertj.assertions;
    exports org.dessertj.classfile;
    exports org.dessertj.classfile.attribute;
    exports org.dessertj.modules;
    exports org.dessertj.modules.core;
    exports org.dessertj.modules.fixed;
    exports org.dessertj.modules.jpms;
    exports org.dessertj.partitioning;
    exports org.dessertj.resolve;
    exports org.dessertj.slicing;
    exports org.dessertj.util;
}
