package com.vgleadsheets.composables.previews.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SheetPageCardListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.ModuleDetails
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.remaster.home.State
import com.vgleadsheets.remaster.home.modules.RngModule
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.StringResources
import java.util.Random
import kotlinx.collections.immutable.persistentListOf

@DevicePreviews
@Composable
internal fun HomeScreen(darkTheme: Boolean = isSystemInDarkTheme()) {
    val stringProvider = StringResources(LocalContext.current.resources)
    val screenState = homeScreenState(stringProvider)
    ListScreenPreview(screenState, darkTheme = darkTheme)
}

@DevicePreviews
@Composable
internal fun HomeScreenLoading(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = homeScreenLoadingState()
    ListScreenPreview(screenState, darkTheme = darkTheme)
}

@Suppress("MagicNumber")
private fun homeScreenState(stringProvider: StringProvider): State {
    val seed = 1234567L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val moduleStatesByPriority = mapOf(
        sheetModule(modelGenerator),
        gameModule(modelGenerator),
        composerModule(modelGenerator),
        rngModule(stringProvider)
    )

    val screenState = State(
        moduleStatesByPriority = moduleStatesByPriority
    )
    return screenState
}

@Suppress("MagicNumber")
private fun homeScreenLoadingState(): State {
    val moduleStatesByPriority = mapOf(
        loadingModule(LoadingType.NOTIF, Priority.HIGHEST),
        loadingModule(LoadingType.SHEET, Priority.HIGH),
        loadingModule(LoadingType.SQUARE, Priority.HIGH),
    )

    val screenState = State(
        moduleStatesByPriority = moduleStatesByPriority
    )
    return screenState
}

private fun rngModule(stringProvider: StringProvider): Pair<ModuleDetails, LCE<HomeModuleState>> {
    val moduleName = "Rng"
    val rngModule = RngModule(
        stringProvider,
        object : DelayManager {
            override fun shouldDelay(): Boolean {
                return false
            }
        }
    )

    val details = ModuleDetails(
        name = moduleName,
        priority = rngModule.priority,
    )

    return details to rngModule.content()
}

private fun sheetModule(modelGenerator: FakeModelGenerator): Pair<ModuleDetails, LCE<HomeModuleState>> {
    val songs = modelGenerator.randomSongs()

    val moduleName = "Songs"
    val priority = Priority.HIGH

    val state = HomeModuleState(
        moduleName = moduleName,
        shouldShow = true,
        title = "Sick Songs",
        items = songs.map { song ->
            SheetPageCardListModel(
                SheetPageListModel(
                    dataId = song.id,
                    title = song.name,
                    sourceInfo = PdfConfigById(
                        songId = song.id,
                        isAltSelected = false,
                        pageNumber = 0,
                    ),
                    gameName = song.gameName,
                    clickAction = VglsAction.Noop,
                    composers = persistentListOf(),
                    pageNumber = 0,
                )
            )
        },
    )
    val lce = LCE.Content(state)
    val details = ModuleDetails(
        name = moduleName,
        priority = priority,
    )
    return details to lce
}

private fun gameModule(modelGenerator: FakeModelGenerator): Pair<ModuleDetails, LCE<HomeModuleState>> {
    val games = modelGenerator.randomGames()

    val moduleName = "Games"
    val priority = Priority.MID

    val state = HomeModuleState(
        moduleName = moduleName,
        shouldShow = true,
        title = "Great Games",
        items = games.map { game ->
            SquareItemListModel(
                dataId = game.id,
                name = game.name,
                sourceInfo = game.photoUrl,
                imagePlaceholder = Icon.ALBUM,
                clickAction = VglsAction.Noop
            )
        },
    )
    val lce = LCE.Content(state)
    val details = ModuleDetails(
        name = moduleName,
        priority = priority,
    )
    return details to lce
}

private fun composerModule(modelGenerator: FakeModelGenerator): Pair<ModuleDetails, LCE<HomeModuleState>> {
    val composers = modelGenerator.randomComposers()

    val moduleName = "Composers"
    val priority = Priority.LOW

    val state = HomeModuleState(
        moduleName = moduleName,
        shouldShow = true,
        title = "Cool Composers",
        items = composers.map { composer ->
            SquareItemListModel(
                dataId = composer.id,
                name = composer.name,
                sourceInfo = composer.photoUrl,
                imagePlaceholder = Icon.PERSON,
                clickAction = VglsAction.Noop
            )
        },
    )
    val lce = LCE.Content(state)
    val details = ModuleDetails(
        name = moduleName,
        priority = priority,
    )
    return details to lce
}

private fun loadingModule(loadingType: LoadingType, priority: Priority): Pair<ModuleDetails, LCE<HomeModuleState>> {
    val moduleName = loadingType.name
    val title = if (loadingType == LoadingType.NOTIF) {
        null
    } else {
        moduleName
    }

    val state = HomeModule.loadingStateFromName(
        moduleName,
        title,
        loadingType
    )

    val lce = LCE.Content(state)
    val details = ModuleDetails(
        name = moduleName,
        priority = priority,
    )
    return details to lce
}
