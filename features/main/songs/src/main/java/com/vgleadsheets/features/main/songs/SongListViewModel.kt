package com.vgleadsheets.features.main.songs

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.args.AllSongsArgs
import com.vgleadsheets.args.SongsByComposerArgs
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class SongListViewModel @AssistedInject constructor(
    @Assisted initialState: SongListState,
    private val repository: Repository
) : MvRxViewModel<SongListState>(initialState) {
    init {
        fetchTitle()
        fetchSongs()
    }

    private fun fetchTitle() = withState { state ->
        val loadOperation = when (state.type) {
            is SongsByComposerArgs -> repository.getComposer(state.id!!).map { it.name }
            is AllSongsArgs -> return@withState
        }

        loadOperation.execute { title ->
            copy(title = title)
        }
    }

    private fun fetchSongs() = withState { state ->
        val loadOperation = when (state.type) {
            is SongsByComposerArgs -> repository.getSongsByComposer(state.id!!)
            is AllSongsArgs -> repository.getAllSongs()
        }

        loadOperation
            .execute { data ->
                copy(songs = data)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SongListState): SongListViewModel
    }

    companion object : MvRxViewModelFactory<SongListViewModel, SongListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SongListState
        ): SongListViewModel? {
            val fragment: SongListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.sheetListViewModelFactory.create(state)
        }
    }
}
