package com.vgleadsheets.jvm

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import coil3.SingletonImageLoader
import com.vgleadsheets.jvm.di.JvmVglsGraph
import com.vgleadsheets.nav.ActivityEvent
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.scaffold.RemasterAppUi
import com.vgleadsheets.strings.LocalVglsStringProvider
import com.vgleadsheets.strings.VglsStringId
import com.vgleadsheets.ui.theme.AppTheme
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.flow.consumeAsFlow
import net.sigmabeta.sage.ui.perf.LocalLogger
import java.awt.Desktop
import java.net.URI

/**
 * Compose Multiplatform entry point for the desktop target. Calls the same [RemasterAppUi] the
 * Android `RemasteredActivity` does — the dividend of Phase 6 (the whole shell is commonMain). The
 * activity's Intent-based nav-event handling (URL open / finish) becomes AWT `Desktop.browse` /
 * `exitApplication` here. Sheet rendering is wired via the graph's Coil [ImageLoader] (PDFBox-backed
 * decoder), registered as the process singleton before the first composition.
 */
fun runDesktop(graph: JvmVglsGraph) = application {
    // Mirror android's ImagesModule: make the graph's PDF ImageLoader the singleton the shared UI's
    // AsyncImage/rememberAsyncImagePainter pick up. Set before any sheet composes.
    SingletonImageLoader.setSafe { graph.imageLoader }
    Window(
        onCloseRequest = ::exitApplication,
        // Source the OS window title from the single string source (app_name), same as Android's
        // manifest android:label — not a hand-typed literal that can drift out of sync.
        title = graph.stringProvider.getString(VglsStringId.APP_NAME),
    ) {
        AppTheme {
            // The Compose Window has no ViewModelStoreOwner; provide one at the root so the shell's
            // chrome VMs (NavViewModel/TopBar/NavBar via metroViewModel) resolve. Per-screen stores
            // are handled inside RemasterAppUi (WithPerScreenViewModelStore).
            val rootStoreOwner = remember {
                object : ViewModelStoreOwner {
                    override val viewModelStore = ViewModelStore()
                }
            }
            CompositionLocalProvider(
                LocalMetroViewModelFactory provides graph.metroViewModelFactory,
                LocalVglsStringProvider provides graph.stringProvider,
                LocalViewModelStoreOwner provides rootStoreOwner,
            ) {
                CompositionLocalProvider(LocalLogger provides graph.hatchet) {
                    val navViewModel = metroViewModel<NavViewModel>()
                    LaunchedEffect(navViewModel) {
                        navViewModel.activityEvents.consumeAsFlow().collect { event ->
                            when (event) {
                                ActivityEvent.Finish -> exitApplication()
                                is ActivityEvent.LaunchUrl -> openUrl(event.url)
                                ActivityEvent.Restart -> Unit
                            }
                        }
                    }
                    RemasterAppUi(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

private fun openUrl(url: String) {
    if (!Desktop.isDesktopSupported()) return
    val desktop = Desktop.getDesktop()
    if (!desktop.isSupported(Desktop.Action.BROWSE)) return
    desktop.browse(URI(url))
}
