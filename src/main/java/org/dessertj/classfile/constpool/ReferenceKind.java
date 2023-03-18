package org.dessertj.classfile.constpool;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2023 Hans Jörg Heßmann
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Bytecode Behaviors for Method Handles.
 * See <a href="https://docs.oracle.com/javase/specs/jvms/se19/html/jvms-5.html#jvms-5.4.3.5" target="_blank">Method Type and Method Handle Resolution</a>.
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

    ReferenceKind(String interpretation) {
        this.interpretation = interpretation;
    }
}
