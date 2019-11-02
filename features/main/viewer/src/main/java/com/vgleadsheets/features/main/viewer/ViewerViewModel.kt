package com.vgleadsheets.features.main.viewer

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.storage.Storage

class ViewerViewModel @AssistedInject constructor(
    @Assisted initialState: ViewerState,
    private val repository: Repository,
    private val storage: Storage
) : MvRxViewModel<ViewerState>(initialState) {
    init {
        fetchSong()
        fetchParts()
        checkScreenSetting()
    }

    fun checkScreenSetting() {
        storage.getSettingSheetScreenOn()
            .execute {
                copy(screenOn = it)
            }
    }

    private fun fetchSong() = withState { state ->
        repository.getSong(state.songId)
            .execute { data ->
                copy(song = data)
            }
    }

    private fun fetchParts() = withState { state ->
        repository.getPartsForSong(state.songId)
            .execute {
                copy(parts = it)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: ViewerState): ViewerViewModel
    }

    companion object : MvRxViewModelFactory<ViewerViewModel, ViewerState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: ViewerState
        ): ViewerViewModel? {
            val fragment: ViewerFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewerViewModelFactory.create(state)
        }
    }
}
