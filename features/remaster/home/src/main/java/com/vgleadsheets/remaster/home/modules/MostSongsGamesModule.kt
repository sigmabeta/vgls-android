package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class MostSongsGamesModule @Inject constructor(
    private val gameRepository: GameRepository,
    private val stringProvider: StringProvider,
    delayManager: DelayManager,
) : HomeModule(
    priority = Priority.LOW,
    delayManager,
) {
    override fun loadingType() = LoadingType.SQUARE

    override fun title() = stringProvider.getString(StringId.HOME_SECTION_MOST_SONGS_GAMES)

    @Suppress("MagicNumber")
    override fun state() = gameRepository
        .getMostSongsGames()
        .map { it.shuffled().take(10) }
        .map { games ->
            LCE.Content(
                HomeModuleState(
                    moduleName = this.javaClass.simpleName,
                    shouldShow = games.isNotEmpty(),
                    title = title(),
                    items = games.map { game ->
                        SquareItemListModel(
                            dataId = game.id,
                            name = game.name,
                            sourceInfo = game.photoUrl,
                            imagePlaceholder = Icon.ALBUM,
                            clickAction = Action.MostSongsGameClicked(game.id)
                        )
                    },
                )
            )
        }
        .withLoadingState()
        .withErrorState()
}
