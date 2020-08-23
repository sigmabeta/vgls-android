package com.vgleadsheets.features.main.tagvalues

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class TagValueListFragment : AsyncListFragment<TagValueData, TagValueListState>() {
    @Inject
    lateinit var tagValueListViewModelFactory: TagValueListViewModel.Factory

    override val viewModel: TagValueListViewModel by fragmentViewModel()

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${idArgs.id}"

    override fun getTrackingScreen() = TrackingScreen.LIST_TAG_VALUE

    override fun getFullLoadTargetTime() = 5000L

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(TagValueListState::clickedListModel) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                showTagValueSongList(clickedId)
                viewModel.clearClicked()
            }
        }
    }

    private fun showTagValueSongList(clickedId: Long) =
        withState(viewModel) { state ->
            val tagValue = state.data.tagValues()?.first { it.id == clickedId }

            if (tagValue == null) {
                showError("Failed to show tag value.")
                return@withState
            }

            getFragmentRouter().showSongListForTagValue(clickedId)
        }

    companion object {
        fun newInstance(idArgs: IdArgs): TagValueListFragment {
            val fragment = TagValueListFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
