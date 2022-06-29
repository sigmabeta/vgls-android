package com.vgleadsheets.features.main.sheet

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class SongViewModel @AssistedInject constructor(
    @Assisted initialState: SongState,
    private val repository: Repository,
) : MvRxViewModel<SongState>(initialState) {
    init {
        fetchSong()
        fetchTagValues()
    }

    private fun fetchSong() = withState {
        repository.getSong(it.songId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        song = it
                    )
                )
            }
    }

    private fun fetchTagValues() = withState {
        repository.getTagValuesForSong(it.songId)
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
            initialState: SongState,
        ): SongViewModel
    }

    companion object : MvRxViewModelFactory<SongViewModel, SongState> {
        const val ID_COMPOSER_MULTIPLE = Long.MAX_VALUE

        const val RATING_MINIMUM = 0
        const val RATING_MAXIMUM = 5

        override fun create(
            viewModelContext: ViewModelContext,
            state: SongState
        ): SongViewModel {
            val fragment: SongFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
