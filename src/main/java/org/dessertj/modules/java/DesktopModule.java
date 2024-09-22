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
class DesktopModule extends FixedModule {

    DesktopModule(Classpath cp) {
        super("java.desktop", "21",
                Slices.of(
                        cp.slice("java.applet.*"),
                        cp.slice("java.awt.*"),
                        cp.slice("java.awt.color.*"),
                        cp.slice("java.awt.desktop.*"),
                        cp.slice("java.awt.dnd.*"),
                        cp.slice("java.awt.event.*"),
                        cp.slice("java.awt.font.*"),
                        cp.slice("java.awt.geom.*"),
                        cp.slice("java.awt.im.*"),
                        cp.slice("java.awt.im.spi.*"),
                        cp.slice("java.awt.image.*"),
                        cp.slice("java.awt.image.renderable.*"),
                        cp.slice("java.awt.print.*"),
                        cp.slice("java.beans.*"),
                        cp.slice("java.beans.beancontext.*"),
                        cp.slice("javax.accessibility.*"),
                        cp.slice("javax.imageio.*"),
                        cp.slice("javax.imageio.event.*"),
                        cp.slice("javax.imageio.metadata.*"),
                        cp.slice("javax.imageio.plugins.bmp.*"),
                        cp.slice("javax.imageio.plugins.jpeg.*"),
                        cp.slice("javax.imageio.plugins.tiff.*"),
                        cp.slice("javax.imageio.spi.*"),
                        cp.slice("javax.imageio.stream.*"),
                        cp.slice("javax.print.*"),
                        cp.slice("javax.print.attribute.*"),
                        cp.slice("javax.print.attribute.standard.*"),
                        cp.slice("javax.print.event.*"),
                        cp.slice("javax.sound.midi.*"),
                        cp.slice("javax.sound.midi.spi.*"),
                        cp.slice("javax.sound.sampled.*"),
                        cp.slice("javax.sound.sampled.spi.*"),
                        cp.slice("javax.swing.*"),
                        cp.slice("javax.swing.border.*"),
                        cp.slice("javax.swing.colorchooser.*"),
                        cp.slice("javax.swing.event.*"),
                        cp.slice("javax.swing.filechooser.*"),
                        cp.slice("javax.swing.plaf.*"),
                        cp.slice("javax.swing.plaf.basic.*"),
                        cp.slice("javax.swing.plaf.metal.*"),
                        cp.slice("javax.swing.plaf.multi.*"),
                        cp.slice("javax.swing.plaf.nimbus.*"),
                        cp.slice("javax.swing.plaf.synth.*"),
                        cp.slice("javax.swing.table.*"),
                        cp.slice("javax.swing.text.*"),
                        cp.slice("javax.swing.text.html.*"),
                        cp.slice("javax.swing.text.html.parser.*"),
                        cp.slice("javax.swing.text.rtf.*"),
                        cp.slice("javax.swing.tree.*"),
                        cp.slice("javax.swing.undo.*")
                ),
                Slices.of(
                        cp.slice("com.sun.accessibility.internal.resources.*"),
                        cp.slice("com.sun.beans.*"),
                        cp.slice("com.sun.beans.decoder.*"),
                        cp.slice("com.sun.beans.editors.*"),
                        cp.slice("com.sun.beans.finder.*"),
                        cp.slice("com.sun.beans.infos.*"),
                        cp.slice("com.sun.beans.introspect.*"),
                        cp.slice("com.sun.beans.util.*"),
                        cp.slice("com.sun.imageio.plugins.bmp.*"),
                        cp.slice("com.sun.imageio.plugins.common.*"),
                        cp.slice("com.sun.imageio.plugins.gif.*"),
                        cp.slice("com.sun.imageio.plugins.jpeg.*"),
                        cp.slice("com.sun.imageio.plugins.png.*"),
                        cp.slice("com.sun.imageio.plugins.tiff.*"),
                        cp.slice("com.sun.imageio.plugins.wbmp.*"),
                        cp.slice("com.sun.imageio.spi.*"),
                        cp.slice("com.sun.imageio.stream.*"),
                        cp.slice("com.sun.java.swing.*"),
                        cp.slice("com.sun.java.swing.plaf.gtk.*"),
                        cp.slice("com.sun.java.swing.plaf.gtk.icons.*"),
                        cp.slice("com.sun.java.swing.plaf.gtk.resources.*"),
                        cp.slice("com.sun.java.swing.plaf.motif.*"),
                        cp.slice("com.sun.java.swing.plaf.motif.icons.*"),
                        cp.slice("com.sun.java.swing.plaf.motif.resources.*"),
                        cp.slice("com.sun.media.sound.*"),
                        cp.slice("com.sun.swing.internal.plaf.basic.resources.*"),
                        cp.slice("com.sun.swing.internal.plaf.metal.resources.*"),
                        cp.slice("com.sun.swing.internal.plaf.synth.resources.*"),
                        cp.slice("java.applet.*"),
                        cp.slice("java.awt.*"),
                        cp.slice("java.awt.color.*"),
                        cp.slice("java.awt.desktop.*"),
                        cp.slice("java.awt.dnd.*"),
                        cp.slice("java.awt.dnd.peer.*"),
                        cp.slice("java.awt.event.*"),
                        cp.slice("java.awt.font.*"),
                        cp.slice("java.awt.geom.*"),
                        cp.slice("java.awt.im.*"),
                        cp.slice("java.awt.im.spi.*"),
                        cp.slice("java.awt.image.*"),
                        cp.slice("java.awt.image.renderable.*"),
                        cp.slice("java.awt.peer.*"),
                        cp.slice("java.awt.print.*"),
                        cp.slice("java.beans.*"),
                        cp.slice("java.beans.beancontext.*"),
                        cp.slice("javax.accessibility.*"),
                        cp.slice("javax.imageio.*"),
                        cp.slice("javax.imageio.event.*"),
                        cp.slice("javax.imageio.metadata.*"),
                        cp.slice("javax.imageio.plugins.bmp.*"),
                        cp.slice("javax.imageio.plugins.jpeg.*"),
                        cp.slice("javax.imageio.plugins.tiff.*"),
                        cp.slice("javax.imageio.spi.*"),
                        cp.slice("javax.imageio.stream.*"),
                        cp.slice("javax.print.*"),
                        cp.slice("javax.print.attribute.*"),
                        cp.slice("javax.print.attribute.standard.*"),
                        cp.slice("javax.print.event.*"),
                        cp.slice("javax.sound.midi.*"),
                        cp.slice("javax.sound.midi.spi.*"),
                        cp.slice("javax.sound.sampled.*"),
                        cp.slice("javax.sound.sampled.spi.*"),
                        cp.slice("javax.swing.*"),
                        cp.slice("javax.swing.beaninfo.images.*"),
                        cp.slice("javax.swing.border.*"),
                        cp.slice("javax.swing.colorchooser.*"),
                        cp.slice("javax.swing.event.*"),
                        cp.slice("javax.swing.filechooser.*"),
                        cp.slice("javax.swing.plaf.*"),
                        cp.slice("javax.swing.plaf.basic.*"),
                        cp.slice("javax.swing.plaf.basic.icons.*"),
                        cp.slice("javax.swing.plaf.metal.*"),
                        cp.slice("javax.swing.plaf.metal.icons.*"),
                        cp.slice("javax.swing.plaf.metal.icons.ocean.*"),
                        cp.slice("javax.swing.plaf.metal.sounds.*"),
                        cp.slice("javax.swing.plaf.multi.*"),
                        cp.slice("javax.swing.plaf.nimbus.*"),
                        cp.slice("javax.swing.plaf.synth.*"),
                        cp.slice("javax.swing.table.*"),
                        cp.slice("javax.swing.text.*"),
                        cp.slice("javax.swing.text.html.*"),
                        cp.slice("javax.swing.text.html.parser.*"),
                        cp.slice("javax.swing.text.rtf.*"),
                        cp.slice("javax.swing.text.rtf.charsets.*"),
                        cp.slice("javax.swing.tree.*"),
                        cp.slice("javax.swing.undo.*"),
                        cp.slice("sun.awt.*"),
                        cp.slice("sun.awt.X11.*"),
                        cp.slice("sun.awt.datatransfer.*"),
                        cp.slice("sun.awt.dnd.*"),
                        cp.slice("sun.awt.event.*"),
                        cp.slice("sun.awt.geom.*"),
                        cp.slice("sun.awt.im.*"),
                        cp.slice("sun.awt.image.*"),
                        cp.slice("sun.awt.resources.*"),
                        cp.slice("sun.awt.resources.cursors.*"),
                        cp.slice("sun.awt.screencast.*"),
                        cp.slice("sun.awt.shell.*"),
                        cp.slice("sun.awt.util.*"),
                        cp.slice("sun.awt.www.content.*"),
                        cp.slice("sun.awt.www.content.audio.*"),
                        cp.slice("sun.awt.www.content.image.*"),
                        cp.slice("sun.font.*"),
                        cp.slice("sun.font.lookup.*"),
                        cp.slice("sun.java2d.*"),
                        cp.slice("sun.java2d.cmm.*"),
                        cp.slice("sun.java2d.cmm.lcms.*"),
                        cp.slice("sun.java2d.cmm.profiles.*"),
                        cp.slice("sun.java2d.loops.*"),
                        cp.slice("sun.java2d.marlin.*"),
                        cp.slice("sun.java2d.marlin.stats.*"),
                        cp.slice("sun.java2d.opengl.*"),
                        cp.slice("sun.java2d.pipe.*"),
                        cp.slice("sun.java2d.pipe.hw.*"),
                        cp.slice("sun.java2d.x11.*"),
                        cp.slice("sun.java2d.xr.*"),
                        cp.slice("sun.print.*"),
                        cp.slice("sun.print.resources.*"),
                        cp.slice("sun.swing.*"),
                        cp.slice("sun.swing.icon.*"),
                        cp.slice("sun.swing.plaf.*"),
                        cp.slice("sun.swing.plaf.synth.*"),
                        cp.slice("sun.swing.table.*"),
                        cp.slice("sun.swing.text.*"),
                        cp.slice("sun.swing.text.html.*")
                ));
    }
}
