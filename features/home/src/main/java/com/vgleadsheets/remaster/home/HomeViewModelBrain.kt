package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.repository.TagRepository
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreen
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.list.ListViewModelBrain
import net.sigmabeta.sage.list.VglsScheduler
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.nav.Destination
import net.sigmabeta.sage.time.ThreeTenTime
import net.sigmabeta.sage.ui.StringProvider
import org.threeten.bp.LocalDate

class HomeViewModelBrain(
    private val stringProvider: StringProvider,
    private val analytics: Analytics,
    private val hatchet: Hatchet,
    private val scheduler: VglsScheduler,
    private val homeModuleProvider: HomeModuleProvider,
    private val tagRepository: TagRepository,
    private val randomRepository: RandomRepository,
    private val threeTenTime: ThreeTenTime,
) : ListViewModelBrain(
    stringProvider,
    analytics,
    hatchet,
    scheduler,
) {
    override val screenIdentifier = AnalyticsScreen.HOME

    override fun initialState() = State()

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> setup()
            is SageAction.Resume -> return
            is SageAction.Pause -> return
            is SageAction.NotifClearClicked -> onNotifClearClicked(action.id)
            is VglsAction.DbSeeWhatsNewClicked -> onDbSeeWhatsNewClicked()
            is VglsAction.AppSeeWhatsNewClicked -> onAppSeeWhatsNewClicked()
            is VglsAction.RefreshDbClicked -> onRefreshDbClicked()
            is Action.MostSongsGameClicked -> onMostSongsGameClicked(action.gameId)
            is Action.MostSongsComposerClicked -> onMostSongsComposerClicked(action.composerId)
            is Action.MostPlaysGameClicked -> onMostPlaysGameClicked(action.gameId)
            is Action.MostPlaysComposerClicked -> onMostPlaysComposerClicked(action.composerId)
            is Action.MostPlaysSongClicked -> onMostPlaysSongClicked(action.songId)
            is Action.MostPlaysTagValueClicked -> onMostPlaysTagValueClicked(action.tagValueId)
            is Action.RecentSongClicked -> onRecentSongClicked(action.songId)
            Action.RandomSongClicked -> onRandomSongClicked()
            Action.RandomGameClicked -> onRandomGameClicked()
            Action.RandomComposerClicked -> onRandomComposerClicked()
            else -> onUnimplementedAction(action)
        }
    }

    private fun onDbSeeWhatsNewClicked() {
        tagRepository.getIdOfPublishDateTagKey()
            .take(1)
            .filterNotNull()
            .flatMapConcat { tagRepository.getTagValuesForTagKey(it) }
            .take(1)
            .map {
                it.maxBy { tagValue ->
                    threeTenTime.localDateFromString(tagValue.name) ?: LocalDate.MIN
                }
            }
            .onEach { tagValue -> navigateTo(Destination.TAGS_VALUES_SONG_LIST.forId(tagValue.id)) }
            .catch { showError(it.message ?: "Unknown error.") }
            .runInBackground()
    }

    private fun onAppSeeWhatsNewClicked() {
        navigateTo(Destination.UPDATES.noArgs())
    }

    private fun onMostSongsGameClicked(gameId: Long) {
        navigateTo(Destination.GAME_DETAIL.forId(gameId))
    }

    private fun onMostSongsComposerClicked(composerId: Long) {
        navigateTo(Destination.COMPOSER_DETAIL.forId(composerId))
    }

    private fun onMostPlaysGameClicked(gameId: Long) {
        navigateTo(Destination.GAME_DETAIL.forId(gameId))
    }

    private fun onMostPlaysComposerClicked(composerId: Long) {
        navigateTo(Destination.COMPOSER_DETAIL.forId(composerId))
    }

    private fun onMostPlaysSongClicked(songId: Long) {
        navigateTo(Destination.SONG_DETAIL.forId(songId))
    }

    private fun onMostPlaysTagValueClicked(tagValueId: Long) {
        navigateTo(Destination.TAGS_VALUES_SONG_LIST.forId(tagValueId))
    }

    private fun onRecentSongClicked(songId: Long) {
        navigateTo(Destination.SONG_DETAIL.forId(songId))
    }

    private fun onRandomSongClicked() {
        randomRepository
            .getRandomSong()
            .onEach { song -> navigateTo(Destination.SONG_DETAIL.forId(song.id)) }
            .catch { showError(it.message ?: "Unknown error.") }
            .runInBackground()
    }

    private fun onRandomGameClicked() {
        randomRepository
            .getRandomGame()
            .onEach { game -> navigateTo(Destination.GAME_DETAIL.forId(game.id)) }
            .catch { showError(it.message ?: "Unknown error.") }
            .runInBackground()
    }

    private fun onRandomComposerClicked() {
        randomRepository
            .getRandomComposer()
            .onEach { composer -> navigateTo(Destination.COMPOSER_DETAIL.forId(composer.id)) }
            .catch { showError(it.message ?: "Unknown error.") }
            .runInBackground()
    }

    private fun navigateTo(destinationString: String) {
        emitEvent(
            SageEvent.NavigateTo(
                destinationString,
                Destination.HOME.destName
            )
        )
    }

    private fun showError(message: String) {
        hatchet.e("Error occurred: $message")
        emitEvent(
            SageEvent.ShowSnackbar(
                message = "An error occurred. Try again after an app update.",
                withDismissAction = false,
                actionDetails = null,
                source = Destination.HOME.destName
            )
        )
    }

    private fun onUnimplementedAction(action: SageAction) {
        emitEvent(
            SageEvent.ShowSnackbar(
                message = "Unimplemented action: $action.",
                withDismissAction = false,
                actionDetails = null,
                source = Destination.HOME.destName
            )
        )
    }

    private fun onNotifClearClicked(id: Long) {
        emitEvent(SageEvent.ClearNotif(id))
    }

    private fun onRefreshDbClicked() {
        emitEvent(VglsEvent.RefreshDb)
    }

    private fun setup() {
        val modulesByPriority = homeModuleProvider
            .modules
            .map { it to it.moduleState }
            .sortedBy { it.first.priority }

        val combinedFlows = modulesByPriority
            .asFlow()
            .flatMapMerge { flowPairing ->
                flowPairing
                    .second
                    .map { state ->
                        flowPairing.first to state
                    }
            }

        combinedFlows
            .onEach { pairing ->
                updateState { oldState ->
                    val state = oldState as State
                    val newModuleStates = state
                        .moduleStatesByPriority
                        .toMutableMap()

                    val moduleDetails = ModuleDetails(
                        pairing.first.javaClass.simpleName,
                        pairing.first.priority
                    )

                    newModuleStates[moduleDetails] = pairing.second

                    state.copy(
                        moduleStatesByPriority = newModuleStates
                    )
                }
            }
            .runInBackground(shouldDelay = false)
    }
}
