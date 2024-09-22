package org.dessertj.modules.java;

/*-
 * #%L
 * DessertJ Dependency Assertion Library for Java
 * %%
 * Copyright (C) 2017 - 2024 Hans Jörg Heßmann
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

import org.dessertj.modules.core.FixedModule;
import org.dessertj.slicing.Classpath;
import org.dessertj.slicing.Slices;

/**
 * Generated by org.dessertj.tools.GenerateStaticModulesTool.
 */
class BaseModule extends FixedModule {

    BaseModule(Classpath cp) {
        super("java.base", "21",
                Slices.of(
                        cp.slice("java.io.*"),
                        cp.slice("java.lang.*"),
                        cp.slice("java.lang.annotation.*"),
                        cp.slice("java.lang.constant.*"),
                        cp.slice("java.lang.foreign.*"),
                        cp.slice("java.lang.invoke.*"),
                        cp.slice("java.lang.module.*"),
                        cp.slice("java.lang.ref.*"),
                        cp.slice("java.lang.reflect.*"),
                        cp.slice("java.lang.runtime.*"),
                        cp.slice("java.math.*"),
                        cp.slice("java.net.*"),
                        cp.slice("java.net.spi.*"),
                        cp.slice("java.nio.*"),
                        cp.slice("java.nio.channels.*"),
                        cp.slice("java.nio.channels.spi.*"),
                        cp.slice("java.nio.charset.*"),
                        cp.slice("java.nio.charset.spi.*"),
                        cp.slice("java.nio.file.*"),
                        cp.slice("java.nio.file.attribute.*"),
                        cp.slice("java.nio.file.spi.*"),
                        cp.slice("java.security.*"),
                        cp.slice("java.security.cert.*"),
                        cp.slice("java.security.interfaces.*"),
                        cp.slice("java.security.spec.*"),
                        cp.slice("java.text.*"),
                        cp.slice("java.text.spi.*"),
                        cp.slice("java.time.*"),
                        cp.slice("java.time.chrono.*"),
                        cp.slice("java.time.format.*"),
                        cp.slice("java.time.temporal.*"),
                        cp.slice("java.time.zone.*"),
                        cp.slice("java.util.*"),
                        cp.slice("java.util.concurrent.*"),
                        cp.slice("java.util.concurrent.atomic.*"),
                        cp.slice("java.util.concurrent.locks.*"),
                        cp.slice("java.util.function.*"),
                        cp.slice("java.util.jar.*"),
                        cp.slice("java.util.random.*"),
                        cp.slice("java.util.regex.*"),
                        cp.slice("java.util.spi.*"),
                        cp.slice("java.util.stream.*"),
                        cp.slice("java.util.zip.*"),
                        cp.slice("javax.crypto.*"),
                        cp.slice("javax.crypto.interfaces.*"),
                        cp.slice("javax.crypto.spec.*"),
                        cp.slice("javax.net.*"),
                        cp.slice("javax.net.ssl.*"),
                        cp.slice("javax.security.auth.*"),
                        cp.slice("javax.security.auth.callback.*"),
                        cp.slice("javax.security.auth.login.*"),
                        cp.slice("javax.security.auth.spi.*"),
                        cp.slice("javax.security.auth.x500.*"),
                        cp.slice("javax.security.cert.*")
                ),
                Slices.of(
                        cp.slice("com.sun.crypto.provider.*"),
                        cp.slice("com.sun.security.ntlm.*"),
                        cp.slice("java.io.*"),
                        cp.slice("java.lang.*"),
                        cp.slice("java.lang.annotation.*"),
                        cp.slice("java.lang.constant.*"),
                        cp.slice("java.lang.foreign.*"),
                        cp.slice("java.lang.foreign.snippets.*"),
                        cp.slice("java.lang.invoke.*"),
                        cp.slice("java.lang.module.*"),
                        cp.slice("java.lang.ref.*"),
                        cp.slice("java.lang.reflect.*"),
                        cp.slice("java.lang.runtime.*"),
                        cp.slice("java.math.*"),
                        cp.slice("java.net.*"),
                        cp.slice("java.net.spi.*"),
                        cp.slice("java.nio.*"),
                        cp.slice("java.nio.channels.*"),
                        cp.slice("java.nio.channels.spi.*"),
                        cp.slice("java.nio.charset.*"),
                        cp.slice("java.nio.charset.spi.*"),
                        cp.slice("java.nio.file.*"),
                        cp.slice("java.nio.file.attribute.*"),
                        cp.slice("java.nio.file.spi.*"),
                        cp.slice("java.security.*"),
                        cp.slice("java.security.cert.*"),
                        cp.slice("java.security.interfaces.*"),
                        cp.slice("java.security.spec.*"),
                        cp.slice("java.text.*"),
                        cp.slice("java.text.spi.*"),
                        cp.slice("java.time.*"),
                        cp.slice("java.time.chrono.*"),
                        cp.slice("java.time.format.*"),
                        cp.slice("java.time.temporal.*"),
                        cp.slice("java.time.zone.*"),
                        cp.slice("java.util.*"),
                        cp.slice("java.util.concurrent.*"),
                        cp.slice("java.util.concurrent.atomic.*"),
                        cp.slice("java.util.concurrent.locks.*"),
                        cp.slice("java.util.function.*"),
                        cp.slice("java.util.jar.*"),
                        cp.slice("java.util.random.*"),
                        cp.slice("java.util.regex.*"),
                        cp.slice("java.util.spi.*"),
                        cp.slice("java.util.stream.*"),
                        cp.slice("java.util.zip.*"),
                        cp.slice("javax.crypto.*"),
                        cp.slice("javax.crypto.interfaces.*"),
                        cp.slice("javax.crypto.spec.*"),
                        cp.slice("javax.net.*"),
                        cp.slice("javax.net.ssl.*"),
                        cp.slice("javax.security.auth.*"),
                        cp.slice("javax.security.auth.callback.*"),
                        cp.slice("javax.security.auth.login.*"),
                        cp.slice("javax.security.auth.spi.*"),
                        cp.slice("javax.security.auth.x500.*"),
                        cp.slice("javax.security.cert.*"),
                        cp.slice("jdk.internal.*"),
                        cp.slice("jdk.internal.access.*"),
                        cp.slice("jdk.internal.access.foreign.*"),
                        cp.slice("jdk.internal.classfile.*"),
                        cp.slice("jdk.internal.classfile.attribute.*"),
                        cp.slice("jdk.internal.classfile.components.*"),
                        cp.slice("jdk.internal.classfile.constantpool.*"),
                        cp.slice("jdk.internal.classfile.impl.*"),
                        cp.slice("jdk.internal.classfile.impl.verifier.*"),
                        cp.slice("jdk.internal.classfile.instruction.*"),
                        cp.slice("jdk.internal.event.*"),
                        cp.slice("jdk.internal.foreign.*"),
                        cp.slice("jdk.internal.foreign.abi.*"),
                        cp.slice("jdk.internal.foreign.abi.aarch64.*"),
                        cp.slice("jdk.internal.foreign.abi.aarch64.linux.*"),
                        cp.slice("jdk.internal.foreign.abi.aarch64.macos.*"),
                        cp.slice("jdk.internal.foreign.abi.aarch64.windows.*"),
                        cp.slice("jdk.internal.foreign.abi.fallback.*"),
                        cp.slice("jdk.internal.foreign.abi.ppc64.*"),
                        cp.slice("jdk.internal.foreign.abi.ppc64.linux.*"),
                        cp.slice("jdk.internal.foreign.abi.riscv64.*"),
                        cp.slice("jdk.internal.foreign.abi.riscv64.linux.*"),
                        cp.slice("jdk.internal.foreign.abi.x64.*"),
                        cp.slice("jdk.internal.foreign.abi.x64.sysv.*"),
                        cp.slice("jdk.internal.foreign.abi.x64.windows.*"),
                        cp.slice("jdk.internal.foreign.layout.*"),
                        cp.slice("jdk.internal.icu.impl.*"),
                        cp.slice("jdk.internal.icu.impl.data.icudt72b.*"),
                        cp.slice("jdk.internal.icu.lang.*"),
                        cp.slice("jdk.internal.icu.text.*"),
                        cp.slice("jdk.internal.icu.util.*"),
                        cp.slice("jdk.internal.io.*"),
                        cp.slice("jdk.internal.javac.*"),
                        cp.slice("jdk.internal.jimage.*"),
                        cp.slice("jdk.internal.jimage.decompressor.*"),
                        cp.slice("jdk.internal.jmod.*"),
                        cp.slice("jdk.internal.jrtfs.*"),
                        cp.slice("jdk.internal.loader.*"),
                        cp.slice("jdk.internal.logger.*"),
                        cp.slice("jdk.internal.math.*"),
                        cp.slice("jdk.internal.misc.*"),
                        cp.slice("jdk.internal.module.*"),
                        cp.slice("jdk.internal.org.objectweb.asm.*"),
                        cp.slice("jdk.internal.org.objectweb.asm.commons.*"),
                        cp.slice("jdk.internal.org.objectweb.asm.signature.*"),
                        cp.slice("jdk.internal.org.objectweb.asm.tree.*"),
                        cp.slice("jdk.internal.org.objectweb.asm.tree.analysis.*"),
                        cp.slice("jdk.internal.org.objectweb.asm.util.*"),
                        cp.slice("jdk.internal.org.xml.sax.*"),
                        cp.slice("jdk.internal.org.xml.sax.helpers.*"),
                        cp.slice("jdk.internal.perf.*"),
                        cp.slice("jdk.internal.platform.*"),
                        cp.slice("jdk.internal.platform.cgroupv1.*"),
                        cp.slice("jdk.internal.platform.cgroupv2.*"),
                        cp.slice("jdk.internal.ref.*"),
                        cp.slice("jdk.internal.reflect.*"),
                        cp.slice("jdk.internal.util.*"),
                        cp.slice("jdk.internal.util.random.*"),
                        cp.slice("jdk.internal.util.regex.*"),
                        cp.slice("jdk.internal.util.xml.*"),
                        cp.slice("jdk.internal.util.xml.impl.*"),
                        cp.slice("jdk.internal.vm.*"),
                        cp.slice("jdk.internal.vm.annotation.*"),
                        cp.slice("jdk.internal.vm.vector.*"),
                        cp.slice("sun.invoke.*"),
                        cp.slice("sun.invoke.empty.*"),
                        cp.slice("sun.invoke.util.*"),
                        cp.slice("sun.launcher.*"),
                        cp.slice("sun.launcher.resources.*"),
                        cp.slice("sun.net.*"),
                        cp.slice("sun.net.dns.*"),
                        cp.slice("sun.net.ext.*"),
                        cp.slice("sun.net.ftp.*"),
                        cp.slice("sun.net.ftp.impl.*"),
                        cp.slice("sun.net.idn.*"),
                        cp.slice("sun.net.sdp.*"),
                        cp.slice("sun.net.smtp.*"),
                        cp.slice("sun.net.spi.*"),
                        cp.slice("sun.net.util.*"),
                        cp.slice("sun.net.www.*"),
                        cp.slice("sun.net.www.content.text.*"),
                        cp.slice("sun.net.www.http.*"),
                        cp.slice("sun.net.www.protocol.file.*"),
                        cp.slice("sun.net.www.protocol.ftp.*"),
                        cp.slice("sun.net.www.protocol.http.*"),
                        cp.slice("sun.net.www.protocol.http.ntlm.*"),
                        cp.slice("sun.net.www.protocol.https.*"),
                        cp.slice("sun.net.www.protocol.jar.*"),
                        cp.slice("sun.net.www.protocol.jmod.*"),
                        cp.slice("sun.net.www.protocol.jrt.*"),
                        cp.slice("sun.net.www.protocol.mailto.*"),
                        cp.slice("sun.nio.*"),
                        cp.slice("sun.nio.ch.*"),
                        cp.slice("sun.nio.cs.*"),
                        cp.slice("sun.nio.fs.*"),
                        cp.slice("sun.reflect.annotation.*"),
                        cp.slice("sun.reflect.generics.factory.*"),
                        cp.slice("sun.reflect.generics.parser.*"),
                        cp.slice("sun.reflect.generics.reflectiveObjects.*"),
                        cp.slice("sun.reflect.generics.repository.*"),
                        cp.slice("sun.reflect.generics.scope.*"),
                        cp.slice("sun.reflect.generics.tree.*"),
                        cp.slice("sun.reflect.generics.visitor.*"),
                        cp.slice("sun.reflect.misc.*"),
                        cp.slice("sun.security.action.*"),
                        cp.slice("sun.security.internal.interfaces.*"),
                        cp.slice("sun.security.internal.spec.*"),
                        cp.slice("sun.security.jca.*"),
                        cp.slice("sun.security.pkcs.*"),
                        cp.slice("sun.security.pkcs10.*"),
                        cp.slice("sun.security.pkcs12.*"),
                        cp.slice("sun.security.provider.*"),
                        cp.slice("sun.security.provider.certpath.*"),
                        cp.slice("sun.security.provider.certpath.ssl.*"),
                        cp.slice("sun.security.rsa.*"),
                        cp.slice("sun.security.ssl.*"),
                        cp.slice("sun.security.timestamp.*"),
                        cp.slice("sun.security.tools.*"),
                        cp.slice("sun.security.tools.keytool.*"),
                        cp.slice("sun.security.util.*"),
                        cp.slice("sun.security.util.math.*"),
                        cp.slice("sun.security.util.math.intpoly.*"),
                        cp.slice("sun.security.validator.*"),
                        cp.slice("sun.security.x509.*"),
                        cp.slice("sun.text.*"),
                        cp.slice("sun.text.resources.*"),
                        cp.slice("sun.text.resources.cldr.*"),
                        cp.slice("sun.text.spi.*"),
                        cp.slice("sun.util.*"),
                        cp.slice("sun.util.calendar.*"),
                        cp.slice("sun.util.cldr.*"),
                        cp.slice("sun.util.locale.*"),
                        cp.slice("sun.util.locale.provider.*"),
                        cp.slice("sun.util.logging.*"),
                        cp.slice("sun.util.resources.*"),
                        cp.slice("sun.util.resources.cldr.*"),
                        cp.slice("sun.util.spi.*")
                ));
    }
}
