package com.vgleadsheets.jvm

import com.vgleadsheets.jvm.di.JvmVglsGraph
import dev.zacsweers.metro.createGraphFactory

/** Desktop entry point — builds the JVM Metro graph and hands it to the Compose window. */
fun main() {
    val graph = createGraphFactory<JvmVglsGraph.Factory>().create()
    runDesktop(graph)
}
