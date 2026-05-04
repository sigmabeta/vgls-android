package com.vgleadsheets.composables.previews.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.ModuleDetails
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.remaster.home.State
import com.vgleadsheets.remaster.home.modules.RngModule
import com.vgleadsheets.scaffold.currentWindowWidthClassSynthetic
import com.vgleadsheets.strings.VglsStringId
import kotlinx.collections.immutable.persistentListOf
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.SheetPageCardListModel
import net.sigmabeta.sage.components.SheetPageListModel
import net.sigmabeta.sage.components.SquareItemListModel
import net.sigmabeta.sage.images.PdfSize
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.list.WidthClass
import net.sigmabeta.sage.pdf.PdfConfigById
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringProvider
import net.sigmabeta.sage.ui.strings.AndroidStringProvider
import java.util.Random

@DevicePreviews
@Composable
internal fun HomeScreen(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val stringProvider = AndroidStringProvider(LocalContext.current.resources) { (it as VglsStringId).id() }
    val screenState = homeScreenState(stringProvider)
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@DevicePreviews
@Composable
internal fun HomeScreenLoading(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = homeScreenLoadingState()
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
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
        loadingModule(LoadingType.PAGE, Priority.HIGH),
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
            override fun shouldDelay(): Boolean = false
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
                    pdfConfigById = PdfConfigById(
                        songId = song.id,
                        isAltSelected = false,
                        pageNumber = 0,
                        pdfSize = PdfSize.MEDIUM,
                    ),
                    gameName = song.gameName,
                    clickAction = SageAction.Noop,
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
                clickAction = SageAction.Noop
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
                clickAction = SageAction.Noop
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
