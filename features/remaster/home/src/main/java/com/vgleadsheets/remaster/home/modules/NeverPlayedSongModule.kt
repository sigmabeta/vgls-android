package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SheetPageCardListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.model.Song
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.time.PublishDateUtils.toLongDateText
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class NeverPlayedSongModule @Inject constructor(
    private val randomRepository: RandomRepository,
    private val songHistoryRepository: SongHistoryRepository,
    private val stringProvider: StringProvider,
    delayManager: DelayManager,
) : HomeModule(
    priority = Priority.LOW,
    delayManager,
) {
    private val appLaunchTime = System.currentTimeMillis()

    override fun loadingType() = LoadingType.SHEET

    override fun title() = stringProvider.getString(StringId.HOME_SECTION_NO_PLAYS_SONGS)

    @Suppress("MagicNumber")
    override fun state(): Flow<LCE<HomeModuleState>> {
        return randomRepository
            .getRandomSongs(20, seed = appLaunchTime.toLongDateText().hashCode().toLong())
            .filter { it.isNotEmpty() }
            .take(1)
            .map { it.onlySongsNeverPlayed() }
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
    }

    private suspend fun Song.wasNeverPlayed(): Boolean {
        val songPlayCount = getSongPlayCount(this)
        return songPlayCount == null
    }

    private suspend fun getSongPlayCount(it: Song) = songHistoryRepository
        .getSongPlayCount(it.id)
        .firstOrNull()
        ?.playCount


    private suspend fun List<Song>.onlySongsNeverPlayed() = filter { it.wasNeverPlayed() }
        .take(10)
        .shuffled()
}


