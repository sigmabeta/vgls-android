package com.vgleadsheets.features.main.jam.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.content
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.tracking.TrackingScreen
import timber.log.Timber

class BetterJamViewModel @AssistedInject constructor(
    @Assisted initialState: BetterJamState,
    @Assisted private val router: FragmentRouter,
    private val repository: Repository,
) : MvRxViewModel<BetterJamState>(initialState) {
    init {
        fetchJam()
    }

    private var firstRefresh = true

    fun onSongClicked(
        id: Long,
        songName: String,
        gameName: String,
        transposition: String
    ) {
        router.showSongViewer(
            id,
            songName,
            gameName,
            transposition,
            TrackingScreen.DETAIL_GAME,
        )
    }

    fun onFollowClicked() = withState {
        router.showJamViewer(it.jamId)
    }

    fun refreshJam() = withState { state ->
        Timber.i("Refreshing jam...")

        val name = state.contentLoad.jam()?.name ?: return@withState
        fireJamRefreshRequest(name, state)
    }

    fun deleteJam() = withState {
        repository.removeJam(it.jamId)
            .execute {
                copy(
                    deletion = it
                )
            }
    }

    private fun fireJamRefreshRequest(
        name: String,
        state: BetterJamState
    ) {
        repository
            .refreshJamState(name)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        jamRefresh = it
                    )
                )
            }

        repository
            .refreshSetlist(state.jamId, name)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        setlistRefresh = it
                    )
                )
            }
    }

    private fun fetchJam() = withState { state ->
        val jamId = state.jamId

        repository.getJam(jamId, true)
            .execute {
                val jamName = it.content()?.name
                if (jamName != null && firstRefresh) {
                    fireJamRefreshRequest(jamName, this)
                    firstRefresh = false
                }

                copy(
                    contentLoad = contentLoad.copy(
                        jam = it
                    )
                )
            }

        repository.getSetlistForJam(jamId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        setlist = it
                    )
                )
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterJamState,
            router: FragmentRouter
        ): BetterJamViewModel
    }

    companion object : MvRxViewModelFactory<BetterJamViewModel, BetterJamState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterJamState
        ): BetterJamViewModel {
            val fragment: BetterJamFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state, fragment.activity as FragmentRouter)
        }
    }
}
