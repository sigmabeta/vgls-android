package com.vgleadsheets.features.main.composers

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.list.ListFragment
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class ComposerListFragment : ListFragment<Composer, ComposerListState>() {
    @Inject
    lateinit var composerListViewModelFactory: ComposerListViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.COMPOSER_LIST

    override val viewModel: ComposerListViewModel by fragmentViewModel()

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(ComposerListState::clickedListModel) {
            val clickedComposerId = it?.dataId

            if (clickedComposerId != null) {
                getFragmentRouter().showSongListForComposer(
                    clickedComposerId,
                    it.name
                )
                viewModel.clearClicked()
            }
        }
    }

    companion object {
        fun newInstance() = ComposerListFragment()
    }
}
