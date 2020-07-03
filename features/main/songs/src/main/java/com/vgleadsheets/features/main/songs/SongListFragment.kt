package com.vgleadsheets.features.main.songs

import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.features.main.list.ListFragment
import com.vgleadsheets.model.song.Song
import javax.inject.Inject

class SongListFragment : ListFragment<Song, SongListState>() {
    @Inject
    lateinit var sheetListViewModelFactory: SongListViewModel.Factory

    override val viewModel: SongListViewModel by fragmentViewModel()

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(SongListState::clickedListModel) {
            val clickedSongId = it?.dataId

            if (clickedSongId != null) {
                showSongViewer(clickedSongId)
                viewModel.clearClicked()
            }
        }
    }

    private fun showSongViewer(clickedSongId: Long) =
        withState(hudViewModel, viewModel) { hudState, state ->
            val song = state.data()?.first { it.id == clickedSongId }

            if (song == null) {
                showError("Failed to show song.")
                return@withState
            }

            tracker.logSongView(
                song.name,
                song.gameName,
                hudState.parts.first { it.selected }.apiId,
                null
            )
            getFragmentRouter().showSheetDetail(clickedSongId)
        }

    companion object {
        fun newInstance() = SongListFragment()
    }
}
