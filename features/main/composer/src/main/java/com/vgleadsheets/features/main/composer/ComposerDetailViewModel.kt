package com.vgleadsheets.features.main.composer

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MavericksViewModel
import com.vgleadsheets.repository.Repository

class ComposerDetailViewModel @AssistedInject constructor(
    @Assisted initialState: ComposerDetailState,
    private val repository: Repository,
) : MavericksViewModel<ComposerDetailState>(initialState) {
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

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: ComposerDetailState
        ): ComposerDetailViewModel
    }

    companion object : MavericksViewModelFactory<ComposerDetailViewModel, ComposerDetailState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: ComposerDetailState
        ): ComposerDetailViewModel {
            val fragment: ComposerDetailFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
