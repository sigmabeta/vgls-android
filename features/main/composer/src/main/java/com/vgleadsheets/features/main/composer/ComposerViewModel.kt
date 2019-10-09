package com.vgleadsheets.features.main.composer

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class ComposerViewModel @AssistedInject constructor(
    @Assisted initialState: ComposerState,
    private val repository: Repository
) : MvRxViewModel<ComposerState>(initialState) {
    init {
        fetchComposer()
        fetchSongs()
    }

    fun onGbComposerNotChecked(vglsId: Long, name: String) {
        repository.searchGiantBombForComposer(vglsId, name)
    }

    private fun fetchComposer() = withState { state ->
        repository
            .getComposer(state.composerId)
            .execute { composer ->
                copy(composer = composer)
            }
    }

    private fun fetchSongs() = withState { state ->
        repository
            .getSongsByComposer(state.composerId)
            .execute { songs ->
                copy(songs = songs)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: ComposerState): ComposerViewModel
    }

    companion object : MvRxViewModelFactory<ComposerViewModel, ComposerState> {
        override fun create(viewModelContext: ViewModelContext, state: ComposerState): ComposerViewModel? {
            val fragment: ComposerFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.composerViewModelFactory.create(state)
        }
    }
}
