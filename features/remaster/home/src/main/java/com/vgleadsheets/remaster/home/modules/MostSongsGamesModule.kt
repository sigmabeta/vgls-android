package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.SquareItemListModel
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
) : HomeModule(
    priority = Priority.LOW,
) {
    override fun state() = gameRepository
        .getMostSongsGames()
        .map { games ->
            LCE.Content(
                HomeModuleState(
                    moduleName = this.javaClass.simpleName,
                    shouldShow = games.isNotEmpty(),
                    priority = priority,
                    title = stringProvider.getString(StringId.HOME_SECTION_MOST_SONGS_GAMES),
                    items = games.map { game ->
                        SquareItemListModel(
                            dataId = game.id,
                            name = game.name,
                            sourceInfo = game.photoUrl,
                            imagePlaceholder = Icon.ALBUM,
                            clickAction = Action.MostSongsGameClicked(game.id)
                        )
                    }
                )
            )
        }
        .withLoadingState()
        .withErrorState()
}
