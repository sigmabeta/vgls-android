package com.vgleadsheets.features.main.tagvalues

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class TagValueViewModel @AssistedInject constructor(
    @Assisted initialState: TagValueState,
    private val repository: VglsRepository,
    private val hatchet: Hatchet,
) : MavericksViewModel<TagValueState>(initialState) {
    init {
        fetchTagKey()
        fetchTagValues()
    }

    private fun fetchTagKey() = withState {
        repository.getTagKey(it.tagKeyId)
            .execute {
                hatchet.w(this.javaClass.simpleName, "Tag Values: ${it()?.values?.size}")
                copy(
                    contentLoad = contentLoad.copy(
                        tagKey = it
                    )
                )
            }
    }

    private fun fetchTagValues() = withState {
        repository.getTagValuesForTagKey(it.tagKeyId)
            .execute { tagValues ->
                copy(
                    contentLoad = contentLoad.copy(
                        tagValues = tagValues
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
