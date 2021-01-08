package de.spricom.dessert.classfile.constpool;

/**
 * Bytecode Behaviors for Method Handles.
 * See <a href="https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-5.html#jvms-5.4.3.5">Method Type and Method Handle Resolution</a>.
 */
public enum ReferenceKind {
    NONE("not defined"),
    REF_getField("getfield C.f:T"),
 	REF_getStatic("getstatic C.f:T"),
 	REF_putField("putfield C.f:T"),
 	REF_putStatic("putstatic C.f:T"),
 	REF_invokeVirtual("invokevirtual C.m:(A*)T"),
 	REF_invokeStatic("invokestatic C.m:(A*)T"),
 	REF_invokeSpecial("invokespecial C.m:(A*)T"),
 	REF_newInvokeSpecial("new C, dup, invokespecial C.<init>:(A*)V"),
 	REF_invokeInterface("invokeinterface C.m:(A*)T");

    public final String interpretation;

    private ReferenceKind(String interpretation) {
        this.interpretation = interpretation;
    }
}
