package com.vgleadsheets.features.main.tagvalues

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class TagValueViewModel @AssistedInject constructor(
    @Assisted initialState: TagValueState,
    private val repository: Repository,
) : MvRxViewModel<TagValueState>(initialState) {
    init {
        fetchTagKey()
        fetchTagValues()
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

    private fun fetchTagValues() = withState {
        repository.getTagValuesForTagKey(it.tagValueId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        tagValues = it
                    )
                )
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: TagValueState,
        ): TagValueViewModel
    }

    companion object : MvRxViewModelFactory<TagValueViewModel, TagValueState> {
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
