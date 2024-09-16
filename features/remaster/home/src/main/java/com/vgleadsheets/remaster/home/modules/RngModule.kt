package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.flowOf

class RngModule @Inject constructor(
    private val stringProvider: StringProvider,
    delayManager: DelayManager,
) : HomeModule(
    priority = Priority.LOWEST,
    delayManager,
) {
    override fun loadingType() = LoadingType.SQUARE

    override fun title() = stringProvider.getString(StringId.HOME_SECTION_RNG)

    override fun state() = flowOf(content())
        .withLoadingState()
        .withErrorState()

    // Public for use in composable previews only
    fun content() = LCE.Content(
        HomeModuleState(
            moduleName = "RngModule",
            shouldShow = true,
            title = title(),
            items = listOf(
                SquareItemListModel(
                    dataId = StringId.HOME_ACTION_RANDOM_SONG.hashCode().toLong(),
                    name = stringProvider.getString(StringId.HOME_ACTION_RANDOM_SONG),
                    sourceInfo = null,
                    imagePlaceholder = Icon.DESCRIPTION,
                    clickAction = Action.RandomSongClicked
                ),
                SquareItemListModel(
                    dataId = StringId.HOME_ACTION_RANDOM_GAME.hashCode().toLong(),
                    name = stringProvider.getString(StringId.HOME_ACTION_RANDOM_GAME),
                    sourceInfo = null,
                    imagePlaceholder = Icon.ALBUM,
                    clickAction = Action.RandomGameClicked
                ),
                SquareItemListModel(
                    dataId = StringId.HOME_ACTION_RANDOM_COMPOSER.hashCode().toLong(),
                    name = stringProvider.getString(StringId.HOME_ACTION_RANDOM_COMPOSER),
                    sourceInfo = null,
                    imagePlaceholder = Icon.PERSON,
                    clickAction = Action.RandomComposerClicked
                ),
            ),
        )
    )
}
