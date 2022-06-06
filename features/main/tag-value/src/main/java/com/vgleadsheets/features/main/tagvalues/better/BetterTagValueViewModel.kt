package com.vgleadsheets.features.main.tagvalues.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class BetterTagValueViewModel @AssistedInject constructor(
    @Assisted initialState: BetterTagValueState,
    @Assisted private val router: FragmentRouter,
    private val repository: Repository,
) : MvRxViewModel<BetterTagValueState>(initialState) {
    init {
        fetchTagKey()
        fetchTagValues()
    }

    fun onTagValueClicked(
        id: Long
    ) {
        router.showSongListForTagValue(
            id
        )
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
            initialState: BetterTagValueState,
            router: FragmentRouter
        ): BetterTagValueViewModel
    }

    companion object : MvRxViewModelFactory<BetterTagValueViewModel, BetterTagValueState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterTagValueState
        ): BetterTagValueViewModel {
            val fragment: BetterTagValueFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state, fragment.activity as FragmentRouter)
        }
    }
}
