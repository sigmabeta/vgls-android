package com.vgleadsheets.features.main.composers.better

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.features.main.list.BetterLists
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class BetterComposerListFragment :
    BetterListFragment<BetterComposerListContent, BetterComposerListState>() {
    @Inject
    lateinit var viewModelFactory: BetterComposerListViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.LIST_COMPOSER

    override fun getPerfSpec() = PerfSpec.COMPOSERS

    override val viewModel: BetterComposerListViewModel by fragmentViewModel()

    override fun generateList(state: BetterComposerListState, hudState: HudState) =
        BetterLists.generateList(
            BetterComposerListConfig(
                state,
                hudState,
                viewModel,
                BetterComposerListClicks,
                perfTracker,
                getPerfSpec(),
                resources
            ),
            resources
        )

    companion object {
        const val LOAD_OPERATION = "loadComposers"

        fun newInstance() = BetterComposerListFragment()
    }
}
