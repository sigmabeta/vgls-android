package com.vgleadsheets.features.main.tagkeys

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class TagKeyViewModel @AssistedInject constructor(
    @Assisted initialState: TagKeyState,
    private val repository: Repository
) : MvRxViewModel<TagKeyState>(initialState) {
    init {
        fetchTags()
    }

    private fun fetchTags() {
        repository.getAllTagKeys()
            .execute { data ->
                copy(tags = data)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: TagKeyState): TagKeyViewModel
    }

    companion object : MvRxViewModelFactory<TagKeyViewModel, TagKeyState> {
        override fun create(viewModelContext: ViewModelContext, state: TagKeyState): TagKeyViewModel? {
            val fragment: TagKeyFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.tagKeyViewModelFactory.create(state)
        }
    }
}
