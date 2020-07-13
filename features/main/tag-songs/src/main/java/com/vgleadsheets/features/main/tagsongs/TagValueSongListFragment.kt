package com.vgleadsheets.features.main.tagsongs

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class TagValueSongListFragment : AsyncListFragment<TagValueSongListData, TagValueSongListState>() {
    @Inject
    lateinit var tagValueViewModelFactory: TagValueSongListViewModel.Factory

    override val viewModel: TagValueSongListViewModel by fragmentViewModel()

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${idArgs.id}"

    override fun getTrackingScreen() = TrackingScreen.TAG_VALUE_SONG_LIST

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(TagValueSongListState::clickedListModel) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                showSongViewer(clickedId)
                viewModel.clearClicked()
            }
        }
    }

    private fun showSongViewer(clickedSongId: Long) =
        withState(viewModel) { state ->
            val song = state.data.songs()?.first { it.id == clickedSongId }

            if (song == null) {
                showError("Failed to show song.")
                return@withState
            }

            getFragmentRouter().showSongViewer(clickedSongId)
        }

    companion object {
        fun newInstance(idArgs: IdArgs): TagValueSongListFragment {
            val fragment = TagValueSongListFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
