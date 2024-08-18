package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class HomeViewModelBrain(
    private val stringProvider: StringProvider,
    private val hatchet: Hatchet,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
    private val scheduler: VglsScheduler,
    private val homeModuleProvider: HomeModuleProvider,
    private val randomRepository: RandomRepository,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    dispatchers,
    coroutineScope
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> setup()
            is VglsAction.Resume -> return
            is VglsAction.NotifClearClicked -> onNotifClearClicked(action.id)
            is VglsAction.RefreshDbClicked -> onRefreshDbClicked()
            is Action.MostSongsGameClicked -> onMostSongsGameClicked(action.gameId)
            is Action.MostSongsComposerClicked -> onMostSongsComposerClicked(action.composerId)
            is Action.MostPlaysGameClicked -> onMostPlaysGameClicked(action.gameId)
            is Action.MostPlaysComposerClicked -> onMostPlaysComposerClicked(action.composerId)
            is Action.MostPlaysSongClicked -> onMostPlaysSongClicked(action.songId)
            is Action.RecentSongClicked -> onRecentSongClicked(action.songId)
            Action.RandomSongClicked -> onRandomSongClicked()
            Action.RandomGameClicked -> onRandomGameClicked()
            Action.RandomComposerClicked -> onRandomComposerClicked()
            else -> onUnimplementedAction(action)
        }
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

    private fun onRecentSongClicked(songId: Long) {
        navigateTo(Destination.SONG_DETAIL.forId(songId))
    }

    private fun onRandomSongClicked() {
        randomRepository
            .getRandomSong()
            .onEach { song -> navigateTo(Destination.SONG_DETAIL.forId(song.id)) }
            .runInBackground()
    }

    private fun onRandomGameClicked() {
        randomRepository
            .getRandomGame()
            .onEach { game -> navigateTo(Destination.GAME_DETAIL.forId(game.id)) }
            .runInBackground()
    }

    private fun onRandomComposerClicked() {
        randomRepository
            .getRandomComposer()
            .onEach { composer -> navigateTo(Destination.COMPOSER_DETAIL.forId(composer.id)) }
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

    @Suppress("MagicNumber")
    private fun <EmissionType> Flow<EmissionType>.runInBackground(
        dispatcher: CoroutineDispatcher = scheduler.dispatchers.disk,
        shouldDelay: Boolean = scheduler.delayManager.shouldDelay()
    ): Job {
        val possiblyDelayedFlow = if (shouldDelay) {
            (
                this.onStart { delay(2000L) }
                )
        } else {
            this
        }

        return possiblyDelayedFlow
            .flowOn(dispatcher)
            .launchIn(coroutineScope)
    }
}
