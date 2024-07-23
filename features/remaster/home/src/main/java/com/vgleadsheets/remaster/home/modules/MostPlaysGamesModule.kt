package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.history.GamePlayCount
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map

class MostPlaysGamesModule @Inject constructor(
    private val songHistoryRepository: SongHistoryRepository,
    private val stringProvider: StringProvider,
    dispatchers: VglsDispatchers,
    coroutineScope: CoroutineScope,
) : HomeModule(
    dispatchers,
    coroutineScope,
) {
    override fun state() = songHistoryRepository
        .getMostPlaysGames()
        .map { list ->
            list.filter { it.first.playCount > 1 }
        }
        .map { pairs ->
            HomeModuleState(
                shouldShow = shouldShow(pairs),
                priority = Priority.HIGH,
                title = stringProvider.getString(StringId.HOME_SECTION_MOST_PLAYS_GAMES),
                items = pairs
                    .map { it.second }
                    .map { game ->
                        SquareItemListModel(
                            dataId = game.id,
                            name = game.name,
                            sourceInfo = game.photoUrl,
                            imagePlaceholder = Icon.ALBUM,
                            clickAction = Action.MostPlaysGameClicked(game.id)
                        )
                    }
            )
        }

    @Suppress("ReturnCount")
    private fun shouldShow(pairs: List<Pair<GamePlayCount, Game>>): Boolean {
        if (pairs.size < MINIMUM_ITEMS) {
            return false
        }

        if (!pairs.areOldEnough()) {
            return false
        }

        return true
    }

    private fun List<Pair<GamePlayCount, Game>>.areOldEnough(): Boolean {
        val currentTime = System.currentTimeMillis()
        return !none {
            (it.first.mostRecentPlay - currentTime) < MINIMUM_AGE_DAYS.toDuration(DurationUnit.DAYS).inWholeMilliseconds
        }
    }

    companion object {
        private const val MINIMUM_ITEMS = 5
        private const val MINIMUM_AGE_DAYS = 3
    }
}
