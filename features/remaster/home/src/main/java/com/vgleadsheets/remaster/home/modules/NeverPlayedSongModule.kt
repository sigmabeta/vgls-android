package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SheetPageCardListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.model.Song
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class NeverPlayedSongModule @Inject constructor(
    private val randomRepository: RandomRepository,
    private val songHistoryRepository: SongHistoryRepository,
    private val stringProvider: StringProvider,
    delayManager: DelayManager,
) : HomeModule(
    priority = Priority.LOW,
    delayManager,
) {
    override fun loadingType() = LoadingType.SHEET

    override fun title() = stringProvider.getString(StringId.HOME_SECTION_NO_PLAYS_SONGS)

    @Suppress("MagicNumber")
    override fun state() = randomRepository
        .getRandomSongs(20)
        .filter { it.isNotEmpty() }
        .take(1)
        .map { list ->
            list
                .filter { onlySongsNeverPlayed(it) }
                .take(10)
                .shuffled()
        }
        .map { songs ->
            LCE.Content(
                HomeModuleState(
                    moduleName = "NeverPlayedSongModule",
                    shouldShow = songs.isNotEmpty(),
                    title = title(),
                    items = songs
                        .map { song ->
                            SheetPageCardListModel(
                                SheetPageListModel(
                                    dataId = song.id,
                                    title = song.name,
                                    sourceInfo = PdfConfigById(
                                        songId = song.id,
                                        pageNumber = 0,
                                        isAltSelected = false,
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

    private suspend fun onlySongsNeverPlayed(it: Song): Boolean {
        val songPlayCount = getSongPlayCount(it)
        return songPlayCount == null
    }

    private suspend fun getSongPlayCount(it: Song) = songHistoryRepository
        .getSongPlayCount(it.id)
        .firstOrNull()
        ?.playCount

    companion object
}
