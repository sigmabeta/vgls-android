package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SheetPageCardListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.history.SongHistoryEntry
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
import kotlinx.coroutines.flow.map

class RecentSongsModule @Inject constructor(
    private val songHistoryRepository: SongHistoryRepository,
    private val stringProvider: StringProvider,
    delayManager: DelayManager,
) : HomeModule(
    priority = Priority.HIGH,
    delayManager,
) {
    override fun loadingType() = LoadingType.SHEET

    override fun title() = stringProvider.getString(StringId.HOME_SECTION_RECENT_SONGS)

    override fun state() = songHistoryRepository
        .getRecentSongs()
        .map { pairs ->
            LCE.Content(
                HomeModuleState(
                    moduleName = this.javaClass.simpleName,
                    shouldShow = shouldShow(pairs),
                    title = title(),
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
                                    clickAction = Action.RecentSongClicked(song.id),
                                    composers = persistentListOf(),
                                    pageNumber = 0,
                                )
                            )
                        },
                )
            )
        }
        .withLoadingState()
        .withErrorState()

    @Suppress("ReturnCount")
    private fun shouldShow(pairs: List<Pair<SongHistoryEntry, Song>>): Boolean {
        if (pairs.size < MINIMUM_ITEMS) {
            return false
        }

        if (!pairs.areNewEnough()) {
            return false
        }

        return true
    }

    private fun List<Pair<SongHistoryEntry, Song>>.areNewEnough(): Boolean {
        val currentTime = System.currentTimeMillis()
        return !none {
            (it.first.timeMs - currentTime) < MAXIMUM_AGE_DAYS.toDuration(DurationUnit.DAYS).inWholeMilliseconds
        }
    }

    companion object {
        private const val MINIMUM_ITEMS = 2
        private const val MAXIMUM_AGE_DAYS = 4
    }
}
