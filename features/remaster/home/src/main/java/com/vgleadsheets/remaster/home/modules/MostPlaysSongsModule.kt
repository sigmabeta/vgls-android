package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SheetPageCardListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.history.SongPlayCount
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.time.TimeUtils
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.map
import org.threeten.bp.Duration
import org.threeten.bp.Instant

class MostPlaysSongsModule @Inject constructor(
    private val songHistoryRepository: SongHistoryRepository,
    private val stringProvider: StringProvider,
    delayManager: DelayManager,
) : HomeModule(
    priority = Priority.HIGH,
    delayManager,
) {
    override fun loadingType() = LoadingType.SHEET

    override fun title() = stringProvider.getString(StringId.HOME_SECTION_MOST_PLAYS_SONGS)

    override fun state() = songHistoryRepository
        .getMostPlaysSongs()
        .map { list ->
            list.filter { it.first.playCount > 1 }
                .shuffled()
                .distinctBy { it.second.id }
        }
        .map { pairs ->
            LCE.Content(
                HomeModuleState(
                    moduleName = "MostPlaysSongsModule",
                    shouldShow = shouldShow(pairs),
                    title = title(),
                    items = pairs
                        .map { it.second }
                        .map { song ->
                            SheetPageCardListModel(
                                SheetPageListModel(
                                    dataId = song.id,
                                    title = song.name,
                                    sourceInfo = SourceInfo(
                                        PdfConfigById(
                                            songId = song.id,
                                            pageNumber = 0,
                                            isAltSelected = false,
                                        )
                                    ),
                                    gameName = song.gameName,
                                    clickAction = Action.MostPlaysSongClicked(song.id),
                                    composers = persistentListOf(),
                                    beeg = false,
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
    private fun shouldShow(pairs: List<Pair<SongPlayCount, Song>>): Boolean {
        if (pairs.size < MINIMUM_ITEMS) {
            return false
        }

        if (!pairs.areOldEnough()) {
            return false
        }

        return true
    }

    private fun List<Pair<SongPlayCount, Song>>.areOldEnough() = none {
        val recordAge = TimeUtils.calculateAgeOf(Instant.ofEpochMilli(it.first.mostRecentPlay))
        val minimumAge = Duration.ofDays(MINIMUM_AGE_DAYS)

        recordAge < minimumAge
    }

    companion object {
        private const val MINIMUM_ITEMS = 5
        private const val MINIMUM_AGE_DAYS = 3L
    }
}
