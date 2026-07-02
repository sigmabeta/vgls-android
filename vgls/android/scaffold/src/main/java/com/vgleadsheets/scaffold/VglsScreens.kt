package com.vgleadsheets.scaffold

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.ScreenKey
import com.vgleadsheets.nav.RoutedScreen
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
import com.vgleadsheets.search.searchViewModel
import com.vgleadsheets.search.SearchScreen as SearchScreenUi
import com.vgleadsheets.ui.licenses.LicenseScreen as LicenseScreenUi
import com.vgleadsheets.ui.licenses.LicenseViewModel
import com.vgleadsheets.ui.list.ListScreenContent
import com.vgleadsheets.ui.viewer.viewerViewModel
import com.vgleadsheets.ui.viewer.ViewerScreen as ViewerScreenUi
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import dev.zacsweers.metrox.viewmodel.metroViewModel
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.list.WidthClass

/**
 * Voyager screen definitions for VGLS. Replaces the AndroidX-nav `NavGraphBuilder` entries
 * (see the deleted `plainViewModelNavEntries` / `*ScreenNavEntry` bodies inlined below). Each
 * [VglsScreen] carries the string [route] that VGLS VMs still emit via `SageEvent.NavigateTo`,
 * and [screenForRoute] parses those strings back into the right screen.
 *
 * Screens read the display width class from [LocalDisplayWidthClass] (provided by RemasterAppUi)
 * since Voyager's `Screen.Content()` takes no parameters.
 */
val LocalDisplayWidthClass = staticCompositionLocalOf<WidthClass> {
    error("LocalDisplayWidthClass not provided")
}

sealed interface VglsScreen : RoutedScreen

// region No-arg list screens

data object HomeScreen : VglsScreen {
    override val route = "home"

    @Composable
    override fun Content() {
        ListScreenContent(metroViewModel<HomeViewModel>(), route, LocalDisplayWidthClass.current, Modifier.fillMaxSize())
    }
}

data object BrowseScreen : VglsScreen {
    override val route = "browse"

    @Composable
    override fun Content() {
        ListScreenContent(metroViewModel<BrowseViewModel>(), route, LocalDisplayWidthClass.current, Modifier.fillMaxSize())
    }
}

data object GamesListScreen : VglsScreen {
    override val route = "games"

    @Composable
    override fun Content() {
        ListScreenContent(metroViewModel<GameListViewModel>(), route, LocalDisplayWidthClass.current, Modifier.fillMaxSize())
    }
}

data object ComposersListScreen : VglsScreen {
    override val route = "composers"

    @Composable
    override fun Content() {
        ListScreenContent(metroViewModel<ComposerListViewModel>(), route, LocalDisplayWidthClass.current, Modifier.fillMaxSize())
    }
}

data object SongsListScreen : VglsScreen {
    override val route = "songs"

    @Composable
    override fun Content() {
        ListScreenContent(metroViewModel<SongListViewModel>(), route, LocalDisplayWidthClass.current, Modifier.fillMaxSize())
    }
}

data object FavoritesScreen : VglsScreen {
    override val route = "favorites"

    @Composable
    override fun Content() {
        ListScreenContent(metroViewModel<FavoritesViewModel>(), route, LocalDisplayWidthClass.current, Modifier.fillMaxSize())
    }
}

data object DifficultyListScreen : VglsScreen {
    override val route = "difficulties"

    @Composable
    override fun Content() {
        ListScreenContent(metroViewModel<DifficultyListViewModel>(), route, LocalDisplayWidthClass.current, Modifier.fillMaxSize())
    }
}

data object TagsListScreen : VglsScreen {
    override val route = "tags"

    @Composable
    override fun Content() {
        ListScreenContent(metroViewModel<TagListViewModel>(), route, LocalDisplayWidthClass.current, Modifier.fillMaxSize())
    }
}

data object PartsScreen : VglsScreen {
    override val route = "parts"

    @Composable
    override fun Content() {
        ListScreenContent(metroViewModel<PartsListViewModel>(), route, LocalDisplayWidthClass.current, Modifier.fillMaxSize())
    }
}

data object MenuScreen : VglsScreen {
    override val route = "menu"

    @Composable
    override fun Content() {
        ListScreenContent(metroViewModel<MenuViewModel>(), route, LocalDisplayWidthClass.current, Modifier.fillMaxSize())
    }
}

data object UpdatesScreen : VglsScreen {
    override val route = "updates"

    @Composable
    override fun Content() {
        ListScreenContent(metroViewModel<UpdatesViewModel>(), route, LocalDisplayWidthClass.current, Modifier.fillMaxSize())
    }
}

data object OfflineScreen : VglsScreen {
    override val route = "offline"

    @Composable
    override fun Content() {
        ListScreenContent(metroViewModel<OfflineContentViewModel>(), route, LocalDisplayWidthClass.current, Modifier.fillMaxSize())
    }
}

data object OfflineUpdatesScreen : VglsScreen {
    override val route = "offline/updates"

    @Composable
    override fun Content() {
        ListScreenContent(metroViewModel<OfflineUpdatesViewModel>(), route, LocalDisplayWidthClass.current, Modifier.fillMaxSize())
    }
}

// endregion

// region Id-arg list screens

data class GameDetailScreen(val id: Long) : VglsScreen {
    override val route get() = "games/$id"
    override val key: ScreenKey get() = route

    @Composable
    override fun Content() {
        ListScreenContent(
            assistedMetroViewModel<GameDetailViewModel, GameDetailViewModel.Factory> { create(id) },
            route,
            LocalDisplayWidthClass.current,
            Modifier.fillMaxSize(),
        )
    }
}

data class ComposerDetailScreen(val id: Long) : VglsScreen {
    override val route get() = "composers/$id"
    override val key: ScreenKey get() = route

    @Composable
    override fun Content() {
        ListScreenContent(
            assistedMetroViewModel<ComposerDetailViewModel, ComposerDetailViewModel.Factory> { create(id) },
            route,
            LocalDisplayWidthClass.current,
            Modifier.fillMaxSize(),
        )
    }
}

data class SongDetailScreen(val id: Long) : VglsScreen {
    override val route get() = "songs/$id"
    override val key: ScreenKey get() = route

    @Composable
    override fun Content() {
        ListScreenContent(
            assistedMetroViewModel<SongDetailViewModel, SongDetailViewModel.Factory> { create(id) },
            route,
            LocalDisplayWidthClass.current,
            Modifier.fillMaxSize(),
        )
    }
}

data class DifficultyValuesScreen(val id: Long) : VglsScreen {
    override val route get() = "difficulties/$id"
    override val key: ScreenKey get() = route

    @Composable
    override fun Content() {
        ListScreenContent(
            assistedMetroViewModel<DifficultyValuesViewModel, DifficultyValuesViewModel.Factory> { create(id) },
            route,
            LocalDisplayWidthClass.current,
            Modifier.fillMaxSize(),
        )
    }
}

data class TagValuesScreen(val id: Long) : VglsScreen {
    override val route get() = "tags/$id"
    override val key: ScreenKey get() = route

    @Composable
    override fun Content() {
        ListScreenContent(
            assistedMetroViewModel<TagValuesViewModel, TagValuesViewModel.Factory> { create(id) },
            route,
            LocalDisplayWidthClass.current,
            Modifier.fillMaxSize(),
        )
    }
}

data class TagValueSongsScreen(val id: Long) : VglsScreen {
    override val route get() = "tags/value/$id"
    override val key: ScreenKey get() = route

    @Composable
    override fun Content() {
        ListScreenContent(
            assistedMetroViewModel<TagValueSongsViewModel, TagValueSongsViewModel.Factory> { create(id) },
            route,
            LocalDisplayWidthClass.current,
            Modifier.fillMaxSize(),
        )
    }
}

// endregion

// region Special screens (inlined from the former *ScreenNavEntry bodies)

data object SearchNavScreen : VglsScreen {
    override val route = "search"

    @Composable
    override fun Content() {
        var searchText by rememberSaveable { mutableStateOf("") }
        val textFieldUpdater = { newText: String -> searchText = newText }

        val viewModel = searchViewModel(textFieldUpdater)

        DisposableEffect(Unit) {
            viewModel.sendAction(SageAction.Resume)

            onDispose {
                viewModel.sendAction(SageAction.Pause)
            }
        }

        BackHandler(true) { viewModel.sendAction(SageAction.DeviceBack) }

        val state by viewModel.uiState.collectAsStateWithLifecycle()
        val showDebug by viewModel.showDebug.collectAsStateWithLifecycle()

        SearchScreenUi(
            query = searchText,
            results = state.toListItems(viewModel.stringProvider),
            textFieldUpdater = textFieldUpdater,
            showDebug = showDebug,
            actionSink = viewModel,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

data class ViewerNavScreen(val id: Long, val page: Long) : VglsScreen {
    override val route get() = "songs/viewer/$id/$page"
    override val key: ScreenKey get() = route

    @Composable
    override fun Content() {
        val viewModel = viewerViewModel(
            idArg = id,
            pageArg = page,
        )

        DisposableEffect(Unit) {
            viewModel.sendAction(SageAction.Resume)

            onDispose {
                viewModel.sendAction(SageAction.Pause)
            }
        }

        BackHandler(true) { viewModel.sendAction(SageAction.DeviceBack) }

        val state by viewModel.uiState.collectAsStateWithLifecycle()
        val showDebug by viewModel.showDebug.collectAsStateWithLifecycle()

        ViewerScreenUi(
            state = state,
            actionSink = viewModel,
            showDebug = showDebug,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

data object LicensesNavScreen : VglsScreen {
    override val route = "licenses"

    @Composable
    override fun Content() {
        val viewModel: LicenseViewModel = metroViewModel()

        DisposableEffect(Unit) {
            viewModel.sendAction(SageAction.Resume)

            onDispose {
                viewModel.sendAction(SageAction.Pause)
            }
        }

        BackHandler(true) { viewModel.sendAction(SageAction.DeviceBack) }

        val state by viewModel.uiState.collectAsStateWithLifecycle()

        LicenseScreenUi(
            state = state,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

// endregion

/**
 * Parses a VGLS route string (as emitted by VMs through `SageEvent.NavigateTo`) into the
 * corresponding [VglsScreen]. Exact matches cover the no-arg / special routes; id-arg routes
 * are disambiguated by their leading segment plus segment count.
 */
internal fun screenForRoute(route: String): VglsScreen {
    val seg = route.split("/")
    return when (route) {
        "home" -> HomeScreen
        "browse" -> BrowseScreen
        "games" -> GamesListScreen
        "composers" -> ComposersListScreen
        "songs" -> SongsListScreen
        "favorites" -> FavoritesScreen
        "difficulties" -> DifficultyListScreen
        "tags" -> TagsListScreen
        "parts" -> PartsScreen
        "menu" -> MenuScreen
        "updates" -> UpdatesScreen
        "offline" -> OfflineScreen
        "offline/updates" -> OfflineUpdatesScreen
        "search" -> SearchNavScreen
        "licenses" -> LicensesNavScreen
        else -> when {
            seg[0] == "games" && seg.size == 2 -> GameDetailScreen(seg[1].toLong())
            seg[0] == "composers" && seg.size == 2 -> ComposerDetailScreen(seg[1].toLong())
            seg[0] == "songs" && seg.size == 4 && seg[1] == "viewer" ->
                ViewerNavScreen(seg[2].toLong(), seg[3].toLong())
            seg[0] == "songs" && seg.size == 2 -> SongDetailScreen(seg[1].toLong())
            seg[0] == "difficulties" && seg.size == 2 -> DifficultyValuesScreen(seg[1].toLong())
            seg[0] == "tags" && seg.size == 3 && seg[1] == "value" -> TagValueSongsScreen(seg[2].toLong())
            seg[0] == "tags" && seg.size == 2 -> TagValuesScreen(seg[1].toLong())
            else -> error("No Voyager screen for route: $route")
        }
    }
}
