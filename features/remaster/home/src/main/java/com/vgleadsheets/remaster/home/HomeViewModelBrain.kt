package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.time.PublishDateUtils
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take

class HomeViewModelBrain(
    private val stringProvider: StringProvider,
    private val hatchet: Hatchet,
    private val scheduler: VglsScheduler,
    private val homeModuleProvider: HomeModuleProvider,
    private val tagRepository: TagRepository,
    private val randomRepository: RandomRepository,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    scheduler,
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> setup()
            is VglsAction.Resume -> return
            is VglsAction.Pause -> return
            is VglsAction.NotifClearClicked -> onNotifClearClicked(action.id)
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
                it.maxBy { tagValue -> PublishDateUtils.ldtFromString(tagValue.name) }
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
            VglsEvent.NavigateTo(
                destinationString,
                Destination.HOME.destName
            )
        )
    }

    private fun showError(message: String) {
        hatchet.e("Error occurred: $message")
        emitEvent(
            VglsEvent.ShowSnackbar(
                message = "An error occurred. Try again after an app update.",
                withDismissAction = false,
                actionDetails = null,
                source = Destination.HOME.destName
            )
        )
    }

    private fun onUnimplementedAction(action: VglsAction) {
        emitEvent(
            VglsEvent.ShowSnackbar(
                message = "Unimplemented action: $action.",
                withDismissAction = false,
                actionDetails = null,
                source = Destination.HOME.destName
            )
        )
    }

    private fun onNotifClearClicked(id: Long) {
        emitEvent(VglsEvent.ClearNotif(id))
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
