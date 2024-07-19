package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map

class MostSongsGamesModule @Inject constructor(
    private val gameRepository: GameRepository,
    private val stringProvider: StringProvider,
    dispatchers: VglsDispatchers,
    coroutineScope: CoroutineScope,
) : HomeModule(
    dispatchers,
    coroutineScope,
) {
    override fun state() = gameRepository
        .getMostSongsGames()
        .map { games ->
            HomeModuleState(
                shouldShow = games.isNotEmpty(),
                priority = Priority.MID,
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
        }
}
