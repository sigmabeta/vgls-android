package com.vgleadsheets.jvm

import com.vgleadsheets.jvm.di.JvmVglsGraph
import dev.zacsweers.metro.createGraphFactory

/** Desktop entry point — builds the JVM Metro graph and hands it to the Compose window. */
fun main() {
    // PDFBox rasterises sheet glyphs on background (Coil) threads while Compose/AWT paints on the
    // event queue; on Linux they collide in the shared X11 XRender glyph cache, throwing
    // "Unknown glyph format: 0" (a known JBR/OpenJDK bug). Disabling the XRender text pipeline forces
    // the stable X11/software path. Must be set before AWT initialises, so it's the first thing here.
    System.setProperty("sun.java2d.xrender", "false")

    val graph = createGraphFactory<JvmVglsGraph.Factory>().create()
    runDesktop(graph)
}
