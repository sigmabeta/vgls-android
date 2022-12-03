package com.vgleadsheets.features.main.jams

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class JamListFragment :
    BetterListFragment<JamListContent, JamListState>() {
    @Inject
    lateinit var viewModelFactory: JamListViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.LIST_JAM

    override fun getPerfSpec() = PerfSpec.JAMS

    override val alwaysShowBack = false

    override val viewModel: JamListViewModel by fragmentViewModel()

    override fun generateListConfig(state: JamListState, hudState: HudState) = Config(
        state,
        Clicks(
            getFragmentRouter()
        ),
        perfTracker,
        getPerfSpec(),
        resources
    )

    override fun onStart() {
        super.onStart()
        viewModel.refreshJams()
    }

    companion object {
        const val LOAD_OPERATION = "loadJams"

        fun newInstance() = JamListFragment()
    }
}
