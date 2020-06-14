package com.vgleadsheets.features.main.tagkeys

import com.airbnb.mvrx.UniqueOnly
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.list.ListFragment
import com.vgleadsheets.model.tag.TagKey
import javax.inject.Inject

class TagKeyFragment : ListFragment<TagKey, TagKeyState>() {
    @Inject
    lateinit var tagKeyViewModelFactory: TagKeyViewModel.Factory

    override val viewModel: TagKeyViewModel by fragmentViewModel()

    private var apiAvailableErrorShown = false

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(TagKeyState::clickedListModel) {
            val clickedTagKeyId = it?.dataId

            if (clickedTagKeyId != null) {
                showSongList(clickedTagKeyId)
                viewModel.clearClicked()
            }
        }

        viewModel.selectSubscribe(
            TagKeyState::gbApiNotAvailable,
            deliveryMode = UniqueOnly("api")
        ) {
            if (it == true) {
                if (!apiAvailableErrorShown) {
                    apiAvailableErrorShown = true
                    showError(getString(R.string.error_no_gb_api))
                }
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
