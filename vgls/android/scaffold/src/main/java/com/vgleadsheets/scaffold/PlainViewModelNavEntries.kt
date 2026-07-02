package com.vgleadsheets.scaffold

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.remaster.home.HomeViewModel
import com.vgleadsheets.ui.list.ListScreenContent
import dev.zacsweers.metrox.viewmodel.metroViewModel
import net.sigmabeta.sage.list.WidthClass

/**
 * AndroidX-nav entries for screens migrated (phase 4) from `ListViewModelBrain` + the generic
 * `ListViewModel` shell to their own plain `VglsListViewModel`. Each entry resolves its VM via
 * `metroViewModel` (or `assistedMetroViewModel` for nav-arg screens) and renders it through the
 * shared [ListScreenContent], replacing the destination's old generic `listScreenEntry` path.
 * Screens are moved here one at a time; the remaining ones still go through the BrainProvider.
 */
fun NavGraphBuilder.homeScreenNavEntry(
    displayWidthClass: WidthClass,
    globalModifier: Modifier,
) {
    composable(Destination.HOME.template()) {
        val viewModel = metroViewModel<HomeViewModel>()
        ListScreenContent(
            viewModel = viewModel,
            screenName = Destination.HOME.destName,
            displayWidthClass = displayWidthClass,
            globalModifier = globalModifier,
        )
    }
}
