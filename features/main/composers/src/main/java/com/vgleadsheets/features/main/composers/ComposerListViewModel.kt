package com.vgleadsheets.features.main.composers

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.model.Composer
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList

class ComposerListViewModel @AssistedInject constructor(
    @Assisted initialState: ComposerListState,
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    ) : MavericksViewModel<ComposerListState>(initialState) {
    init {
        fetchComposers()
    }

    private fun fetchComposers() {
        repository.getAllComposers()
            .execute {
                copy(composers = it)
            }

        onAsync(
            ComposerListState::composers,
            onSuccess = ::fetchSongsForComposers
        )
    }

    @OptIn(FlowPreview::class)
    private suspend fun fetchSongsForComposers(composers: List<Composer>) {
        suspend {
            composers
                .asFlow()
                .flatMapMerge { composer ->
                    repository.getSongsForComposer(composer.id)
                        .map { composer to it }
                        .take(1)
                }
                .toList()
                .associate { it }
        }.execute {
            copy(composerToSongListMap = it)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            initialState: ComposerListState
        ): ComposerListViewModel
    }

    companion object : MavericksViewModelFactory<ComposerListViewModel, ComposerListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: ComposerListState
        ): ComposerListViewModel {
            val fragment: ComposerListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
