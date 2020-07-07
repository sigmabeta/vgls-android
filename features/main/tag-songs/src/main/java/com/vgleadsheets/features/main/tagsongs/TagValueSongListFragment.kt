package com.vgleadsheets.features.main.tagsongs

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import javax.inject.Inject

class TagValueSongListFragment : AsyncListFragment<TagValueSongListData, TagValueSongListState>() {
    @Inject
    lateinit var tagValueViewModelFactory: TagValueSongListViewModel.Factory

    override val viewModel: TagValueSongListViewModel by fragmentViewModel()

    private val idArgs: IdArgs by args()

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${idArgs.id}"

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
        withState(hudViewModel, viewModel) { hudState, state ->
            val song = state.data.songs()?.first { it.id == clickedSongId }

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
