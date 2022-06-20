package com.vgleadsheets.features.main.composer.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.tracking.TrackingScreen

class BetterComposerViewModel @AssistedInject constructor(
    @Assisted initialState: BetterComposerState,
    @Assisted private val router: FragmentRouter,
    private val repository: Repository,
) : MvRxViewModel<BetterComposerState>(initialState) {
    init {
        fetchComposer()
        fetchSongs()
    }

    private fun fetchComposer() = withState {
        repository.getComposer(it.composerId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        composer = it
                    )
                )
            }
    }

    private fun fetchSongs() = withState {
        repository.getSongsByComposer(it.composerId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        songs = it
                    )
                )
            }
    }

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
            TrackingScreen.DETAIL_COMPOSER,
        )
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterComposerState,
            router: FragmentRouter
        ): BetterComposerViewModel
    }

    companion object : MvRxViewModelFactory<BetterComposerViewModel, BetterComposerState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterComposerState
        ): BetterComposerViewModel {
            val fragment: BetterComposerFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state, fragment.activity as FragmentRouter)
        }
    }
}
