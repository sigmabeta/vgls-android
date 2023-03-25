package com.vgleadsheets.features.main.composer

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class ComposerDetailViewModel @AssistedInject constructor(
    @Assisted initialState: ComposerDetailState,
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
) : MavericksViewModel<ComposerDetailState>(initialState) {
    init {
        fetchComposer()
    }

    fun onFavoriteClick() = withState { state ->
        viewModelScope.launch(dispatchers.disk) {
            repository.toggleFavoriteComposer(
                state.composerId
            )
        }
    }

    private fun fetchComposer() = withState { state ->
        repository.getComposer(state.composerId)
            .execute { composer ->
                copy(
                    contentLoad = contentLoad.copy(
                        composer = composer
                    )
                )
            }
    }

    @AssistedFactory
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
