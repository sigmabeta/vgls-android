package com.vgleadsheets.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.remaster.browse.BrowseViewModel
import com.vgleadsheets.remaster.composers.detail.ComposerDetailViewModel
import com.vgleadsheets.remaster.composers.list.ComposerListViewModel
import com.vgleadsheets.remaster.difficulty.list.DifficultyListViewModel
import com.vgleadsheets.remaster.difficulty.values.DifficultyValuesViewModel
import com.vgleadsheets.remaster.favorites.FavoritesViewModel
import com.vgleadsheets.remaster.games.detail.GameDetailViewModel
import com.vgleadsheets.remaster.games.list.GameListViewModel
import com.vgleadsheets.remaster.home.HomeViewModel
import com.vgleadsheets.remaster.menu.MenuViewModel
import com.vgleadsheets.remaster.offline.content.OfflineContentViewModel
import com.vgleadsheets.remaster.offline.updates.OfflineUpdatesViewModel
import com.vgleadsheets.remaster.parts.PartsListViewModel
import com.vgleadsheets.remaster.songs.detail.SongDetailViewModel
import com.vgleadsheets.remaster.songs.list.SongListViewModel
import com.vgleadsheets.remaster.tags.list.TagListViewModel
import com.vgleadsheets.remaster.tags.songs.TagValueSongsViewModel
import com.vgleadsheets.remaster.tags.values.TagValuesViewModel
import com.vgleadsheets.remaster.updates.UpdatesViewModel
import com.vgleadsheets.ui.list.ListScreenContent
import com.vgleadsheets.viewmodel.list.VglsListViewModel
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel
import net.sigmabeta.sage.list.WidthClass
import net.sigmabeta.sage.nav.ARG_TEMPLATE_ONE

/**
 * AndroidX-nav entries for the screens migrated (phase 4) from `ListViewModelBrain` + the generic
 * `ListViewModel` shell + `BrainProvider` to their own plain [VglsListViewModel]. Each entry resolves
 * its VM via `metroViewModel` (no-arg) or `assistedMetroViewModel` (Long nav-arg) and renders it
 * through the shared [ListScreenContent]. This is the whole list-screen graph now — the old generic
 * `listScreenEntry`/BrainProvider path is gone.
 */
fun NavGraphBuilder.plainViewModelNavEntries(
    displayWidthClass: WidthClass,
    globalModifier: Modifier,
) {
    noArgEntry(Destination.HOME, displayWidthClass, globalModifier) { metroViewModel<HomeViewModel>() }
    noArgEntry(Destination.BROWSE, displayWidthClass, globalModifier) { metroViewModel<BrowseViewModel>() }
    noArgEntry(Destination.GAMES_LIST, displayWidthClass, globalModifier) { metroViewModel<GameListViewModel>() }
    noArgEntry(Destination.COMPOSERS_LIST, displayWidthClass, globalModifier) { metroViewModel<ComposerListViewModel>() }
    noArgEntry(Destination.SONGS_LIST, displayWidthClass, globalModifier) { metroViewModel<SongListViewModel>() }
    noArgEntry(Destination.FAVORITES, displayWidthClass, globalModifier) { metroViewModel<FavoritesViewModel>() }
    noArgEntry(Destination.DIFFICULTY_LIST, displayWidthClass, globalModifier) { metroViewModel<DifficultyListViewModel>() }
    noArgEntry(Destination.TAGS_LIST, displayWidthClass, globalModifier) { metroViewModel<TagListViewModel>() }
    noArgEntry(Destination.PART_PICKER, displayWidthClass, globalModifier) { metroViewModel<PartsListViewModel>() }
    noArgEntry(Destination.MENU, displayWidthClass, globalModifier) { metroViewModel<MenuViewModel>() }
    noArgEntry(Destination.UPDATES, displayWidthClass, globalModifier) { metroViewModel<UpdatesViewModel>() }
    noArgEntry(Destination.OFFLINE, displayWidthClass, globalModifier) { metroViewModel<OfflineContentViewModel>() }
    noArgEntry(Destination.OFFLINE_UPDATES, displayWidthClass, globalModifier) { metroViewModel<OfflineUpdatesViewModel>() }

    idArgEntry(Destination.GAME_DETAIL, displayWidthClass, globalModifier) { id ->
        assistedMetroViewModel<GameDetailViewModel, GameDetailViewModel.Factory> { create(id) }
    }
    idArgEntry(Destination.COMPOSER_DETAIL, displayWidthClass, globalModifier) { id ->
        assistedMetroViewModel<ComposerDetailViewModel, ComposerDetailViewModel.Factory> { create(id) }
    }
    idArgEntry(Destination.SONG_DETAIL, displayWidthClass, globalModifier) { id ->
        assistedMetroViewModel<SongDetailViewModel, SongDetailViewModel.Factory> { create(id) }
    }
    idArgEntry(Destination.DIFFICULTY_VALUES_LIST, displayWidthClass, globalModifier) { id ->
        assistedMetroViewModel<DifficultyValuesViewModel, DifficultyValuesViewModel.Factory> { create(id) }
    }
    idArgEntry(Destination.TAGS_VALUES_LIST, displayWidthClass, globalModifier) { id ->
        assistedMetroViewModel<TagValuesViewModel, TagValuesViewModel.Factory> { create(id) }
    }
    idArgEntry(Destination.TAGS_VALUES_SONG_LIST, displayWidthClass, globalModifier) { id ->
        assistedMetroViewModel<TagValueSongsViewModel, TagValueSongsViewModel.Factory> { create(id) }
    }
}

private fun NavGraphBuilder.noArgEntry(
    destination: Destination,
    displayWidthClass: WidthClass,
    globalModifier: Modifier,
    viewModel: @Composable () -> VglsListViewModel<*>,
) {
    composable(destination.template()) {
        ListScreenContent(
            viewModel = viewModel(),
            screenName = destination.destName,
            displayWidthClass = displayWidthClass,
            globalModifier = globalModifier,
        )
    }
}

private fun NavGraphBuilder.idArgEntry(
    destination: Destination,
    displayWidthClass: WidthClass,
    globalModifier: Modifier,
    viewModel: @Composable (Long) -> VglsListViewModel<*>,
) {
    composable(
        route = destination.template(),
        arguments = listOf(navArgument(ARG_TEMPLATE_ONE) { type = NavType.LongType }),
    ) { entry ->
        val id = entry.arguments?.getLong(ARG_TEMPLATE_ONE) ?: 0L
        ListScreenContent(
            viewModel = viewModel(id),
            screenName = destination.destName,
            displayWidthClass = displayWidthClass,
            globalModifier = globalModifier,
        )
    }
}
