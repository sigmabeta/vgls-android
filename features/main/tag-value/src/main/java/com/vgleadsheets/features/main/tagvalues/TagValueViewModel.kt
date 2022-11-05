package com.vgleadsheets.features.main.tagvalues

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class TagValueViewModel @AssistedInject constructor(
    @Assisted initialState: TagValueState,
    private val repository: VglsRepository,
) : MavericksViewModel<TagValueState>(initialState) {
    init {
        fetchTagKey()
    }

    private fun fetchTagKey() = withState {
        repository.getTagKey(it.tagValueId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        tagKey = it
                    )
                )
            }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            initialState: TagValueState,
        ): TagValueViewModel
    }

    companion object : MavericksViewModelFactory<TagValueViewModel, TagValueState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: TagValueState
        ): TagValueViewModel {
            val fragment: TagValueFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
