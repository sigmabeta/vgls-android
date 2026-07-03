package com.vgleadsheets.remaster

import android.app.Activity
import com.vgleadsheets.pdf.subsample.PdfSubsampleSource
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

/**
 * Narrow accessor surface the [RemasteredActivity] reads off the `Application`. Because the Activity
 * lives in this library module (not the `:app` module that owns the Metro `VglsAppGraph`), it can't
 * reference the graph directly — so `VglsApplication` implements this interface and the Activity does
 * `application as ActivityGraph`. This is the Metro replacement for Hilt's `@AndroidEntryPoint`
 * member injection (see Chipbox's ChipboxServiceGraph/ArtworkProviderGraph pattern).
 */
interface ActivityGraph {
    val hatchet: Hatchet
    val activityDependencyInitializer: ActivityDependencyInitializer
    val pdfSubsampleSourceFactory: PdfSubsampleSource.Factory

    /** Provided as `LocalVglsStringProvider` at the Compose root so composables resolve `text()`. */
    val stringProvider: StringProvider

    /** Backs `metroViewModel()` / `assistedMetroViewModel()` in composition and `by viewModels { }`. */
    val metroViewModelFactory: MetroViewModelFactory

    /** Bind/unbind the current Activity so the (AppScope) wake-lock manager can toggle window flags. */
    fun bindActivity(activity: Activity)
    fun unbindActivity()
}
