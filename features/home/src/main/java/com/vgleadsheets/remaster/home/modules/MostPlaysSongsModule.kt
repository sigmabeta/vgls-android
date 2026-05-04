package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.model.Song
import com.vgleadsheets.model.history.SongPlayCount
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.strings.StringId
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.map
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.SheetPageCardListModel
import net.sigmabeta.sage.components.SheetPageListModel
import net.sigmabeta.sage.images.PdfSize
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.pdf.PdfConfigById
import net.sigmabeta.sage.time.TimeUtils
import net.sigmabeta.sage.ui.StringProvider
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import javax.inject.Inject

class MostPlaysSongsModule @Inject constructor(
    private val songHistoryRepository: SongHistoryRepository,
    private val stringProvider: StringProvider,
    delayManager: DelayManager,
) : HomeModule(
    priority = Priority.HIGH,
    delayManager,
) {
    override fun loadingType() = LoadingType.PAGE

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
                                    pdfConfigById = PdfConfigById(
                                        songId = song.id,
                                        pageNumber = 0,
                                        isAltSelected = false,
                                        pdfSize = PdfSize.MEDIUM,
                                    ),
                                    gameName = song.gameName,
                                    clickAction = Action.MostPlaysSongClicked(song.id),
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
