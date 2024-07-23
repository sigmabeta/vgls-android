package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.components.SheetPageCardListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.history.SongPlayCount
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map

class MostPlaysSongsModule @Inject constructor(
    private val songHistoryRepository: SongHistoryRepository,
    private val stringProvider: StringProvider,
    dispatchers: VglsDispatchers,
    coroutineScope: CoroutineScope,
) : HomeModule(
    dispatchers,
    coroutineScope,
) {
    override fun state() = songHistoryRepository
        .getMostPlaysSongs()
        .map { list ->
            list.filter { it.first.playCount > 1 }
        }
        .map { pairs ->
            HomeModuleState(
                shouldShow = shouldShow(pairs),
                priority = Priority.HIGH,
                title = stringProvider.getString(StringId.HOME_SECTION_MOST_PLAYS_SONGS),
                items = pairs
                    .map { it.second }
                    .map { song ->
                        SheetPageCardListModel(
                            SheetPageListModel(
                                dataId = song.id,
                                title = song.name,
                                sourceInfo = PdfConfigById(
                                    songId = song.id,
                                    pageNumber = 0,
                                ),
                                gameName = song.gameName,
                                clickAction = Action.MostPlaysSongClicked(song.id),
                                composers = persistentListOf(),
                                pageNumber = 0,
                            )
                        )
                    }
            )
        }

    @Suppress("ReturnCount")
    private fun shouldShow(pairs: List<Pair<SongPlayCount, Song>>): Boolean {
        if (pairs.size < MINIMUM_ITEMS) {
            return false
        }

        if (!pairs.areOldEnough()) {
            return false
        }

        return true
    }

    private fun List<Pair<SongPlayCount, Song>>.areOldEnough(): Boolean {
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
