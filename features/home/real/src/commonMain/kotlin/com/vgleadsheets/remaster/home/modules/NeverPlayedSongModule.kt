package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.model.Song
import com.vgleadsheets.remaster.home.Action
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.strings.VglsStringId
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.SheetPageCardListModel
import net.sigmabeta.sage.components.SheetPageListModel
import net.sigmabeta.sage.images.PdfSize
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.pdf.PdfConfigById
import net.sigmabeta.sage.time.TimeProvider
import net.sigmabeta.sage.ui.StringProvider
import dev.zacsweers.metro.Inject

class NeverPlayedSongModule @Inject constructor(
    private val randomRepository: RandomRepository,
    private val songHistoryRepository: SongHistoryRepository,
    private val stringProvider: StringProvider,
    private val threeTenTime: TimeProvider,
    delayManager: DelayManager,
) : HomeModule(
    priority = Priority.LOW,
    delayManager,
) {
    private val appLaunchTime = System.currentTimeMillis()

    override fun loadingType() = LoadingType.PAGE

    override fun title() = stringProvider.getString(VglsStringId.HOME_SECTION_NO_PLAYS_SONGS)

    @Suppress("MagicNumber")
    override fun state(): Flow<LCE<HomeModuleState>> = randomRepository
            .getRandomSongs(20, seed = threeTenTime.longDateTextFromMillis(appLaunchTime).hashCode().toLong())
            .filter {
                it.isNotEmpty()
            }
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

    private suspend fun Song.wasNeverPlayed(): Boolean {
        val songPlayCount = getSongPlayCount(this)
        return songPlayCount == null
    }

    private suspend fun getSongPlayCount(it: Song) = songHistoryRepository
        .getSongPlayCount(it.id)
        .firstOrNull()
        ?.playCount

    @Suppress("MagicNumber")
    private suspend fun List<Song>.onlySongsNeverPlayed() = filter { it.wasNeverPlayed() }
        .take(10)
        .shuffled()
}
