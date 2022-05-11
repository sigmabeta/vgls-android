package com.vgleadsheets.features.main.tagkeys

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.list.ListFragment
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class TagKeyFragment : ListFragment<TagKey, TagKeyState>() {
    @Inject
    lateinit var tagKeyViewModelFactory: TagKeyViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.LIST_TAG_KEY

    override fun getPerfSpec() = PerfSpec.TAG_KEY

    override val viewModel: TagKeyViewModel by fragmentViewModel()

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(TagKeyState::clickedListModel) {
            val clickedTagKeyId = it?.dataId

            if (clickedTagKeyId != null) {
                showSongList(clickedTagKeyId)
                viewModel.clearClicked()
            }
        }
    }

    private fun showSongList(clickedTagKeyId: Long) {
        getFragmentRouter().showValueListForTagKey(clickedTagKeyId)
    }

    companion object {
        fun newInstance() = TagKeyFragment()
    }
}
