package com.vgleadsheets.features.main.songs

import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.features.main.list.ListFragment
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class SongListFragment : ListFragment<Song, SongListState>() {
    @Inject
    lateinit var sheetListViewModelFactory: SongListViewModel.Factory

    override val viewModel: SongListViewModel by fragmentViewModel()

    override fun getTrackingScreen() = TrackingScreen.LIST_SHEET

    override fun getFullLoadTargetTime() = 5000L

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(SongListState::clickedListModel) {
            val clickedSongId = it?.dataId

            if (clickedSongId != null) {
                showSongViewer(clickedSongId)
                viewModel.clearClicked()
            }
        }
    }

    private fun showSongViewer(clickedSongId: Long) = withState(viewModel, hudViewModel) { state, hudState ->
        val song = state.data()?.first { it.id == clickedSongId }

        if (song == null) {
            showError("Failed to show song.")
            return@withState
        }

        val transposition = hudState
            .parts
            .firstOrNull { it.selected }
            ?.apiId ?: "Error"

        getFragmentRouter().showSongViewer(
            clickedSongId,
            song.name,
            song.gameName,
            transposition
        )
    }

    companion object {
        fun newInstance() = SongListFragment()
    }
}
