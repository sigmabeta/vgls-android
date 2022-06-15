package com.vgleadsheets.features.main.jams.better

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.features.main.list.BetterLists
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class BetterJamListFragment :
    BetterListFragment<BetterJamListContent, BetterJamListState>() {
    @Inject
    lateinit var viewModelFactory: BetterJamListViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.LIST_JAM

    override fun getPerfSpec() = PerfSpec.JAMS

    override val viewModel: BetterJamListViewModel by fragmentViewModel()

    override fun generateList(state: BetterJamListState, hudState: HudState) =
        BetterLists.generateList(
            BetterJamListConfig(
                state,
                hudState,
                viewModel,
                BetterJamListClicks,
                perfTracker,
                getPerfSpec(),
                resources
            ),
            resources
        )

    companion object {
        const val LOAD_OPERATION = "loadJams"

        fun newInstance() = BetterJamListFragment()
    }
}
