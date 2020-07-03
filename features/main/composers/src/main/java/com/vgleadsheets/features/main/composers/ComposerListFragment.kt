package com.vgleadsheets.features.main.composers

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.list.ListFragment
import com.vgleadsheets.model.composer.Composer
import javax.inject.Inject

class ComposerListFragment : ListFragment<Composer, ComposerListState>() {
    @Inject
    lateinit var composerListViewModelFactory: ComposerListViewModel.Factory

    override val viewModel: ComposerListViewModel by fragmentViewModel()

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(ComposerListState::clickedListModel) {
            val clickedComposerId = it?.dataId

            if (clickedComposerId != null) {
                showSongList(clickedComposerId)
                viewModel.clearClicked()
            }
        }
    }

    private fun showSongList(clickedComposerId: Long) {
        getFragmentRouter().showSongListForComposer(clickedComposerId)
    }

    companion object {
        fun newInstance() = ComposerListFragment()
    }
}
