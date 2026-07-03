package com.vgleadsheets.jvm.di

import coil3.ImageLoader
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

/**
 * JVM/desktop application Metro graph — the analog of the Android VglsAppGraph, minus the Android
 * Context/Application. Owns every AppScope binding (the same @ContributesTo modules on the JVM
 * classpath) and exposes the accessors DesktopMain reads at the Compose root.
 */
@SingleIn(AppScope::class)
@DependencyGraph(AppScope::class)
interface JvmVglsGraph : ViewModelGraph {
    // metroViewModelFactory is inherited from ViewModelGraph (as in the Android VglsAppGraph).
    val hatchet: Hatchet
    val stringProvider: StringProvider

    // The desktop Coil ImageLoader (PDF sheet pipeline). DesktopMain registers it as the singleton
    // loader so the shared UI's AsyncImage/rememberAsyncImagePainter resolve — the JVM analog of
    // android's ImagesModule side-effecting SingletonImageLoader.setSafe.
    val imageLoader: ImageLoader

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(): JvmVglsGraph
    }
}
