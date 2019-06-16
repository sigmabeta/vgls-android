package com.vgleadsheets.viewer

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class ViewerViewModel @AssistedInject constructor(
    @Assisted initialState: ViewerState,
    private val repository: Repository
) : MvRxViewModel<ViewerState>(initialState) {
    init {
        fetchSong()
    }

    private fun fetchSong() = withState { state ->
        repository.getSongImageUrl(state.songId)
            .execute { data ->
                copy(data = data)
            }
    }


    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: ViewerState): ViewerViewModel
    }

    companion object : MvRxViewModelFactory<ViewerViewModel, ViewerState> {
        override fun create(viewModelContext: ViewModelContext, state: ViewerState): ViewerViewModel? {
            val fragment: ViewerFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewerViewModelFactory.create(state)
        }
    }
}