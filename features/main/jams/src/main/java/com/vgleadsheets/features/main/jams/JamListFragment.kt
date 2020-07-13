package com.vgleadsheets.features.main.jams

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.list.ListFragment
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class JamListFragment : ListFragment<Jam, JamListState>() {
    @Inject
    lateinit var jamListViewModelFactory: JamListViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.JAM_LIST

    override val viewModel: JamListViewModel by fragmentViewModel()

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(JamListState::clickedJamModel) {
            val clickedJamId = it?.dataId

            if (clickedJamId != null) {
                showSongList(clickedJamId)
                viewModel.clearClicked()
            }
        }

        viewModel.selectSubscribe(JamListState::clickedCtaModel) {
            val clickedCtaId = it?.dataId

            if (clickedCtaId != null) {
                getFragmentRouter().showFindJamDialog()
                viewModel.clearClicked()
            }
        }
    }

    private fun showSongList(clickedJamId: Long) {
        getFragmentRouter().showJamDetailViewer(clickedJamId)
    }

    companion object {
        fun newInstance() = JamListFragment()
    }
}
