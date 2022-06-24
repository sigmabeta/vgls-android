package com.vgleadsheets.features.main.composers

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.features.main.list.BetterLists
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class ComposerListFragment :
    BetterListFragment<ComposerListContent, ComposerListState>() {
    @Inject
    lateinit var viewModelFactory: ComposerListViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.LIST_COMPOSER

    override fun getPerfSpec() = PerfSpec.COMPOSERS

    override val viewModel: ComposerListViewModel by fragmentViewModel()

    override fun generateList(state: ComposerListState, hudState: HudState) =
        BetterLists.generateList(
            Config(
                state,
                hudState,
                viewModel,
                Clicks(
                    viewModel,
                    getFragmentRouter(),
                    tracker,
                    ""
                ),
                perfTracker,
                getPerfSpec(),
                resources
            ),
            resources
        )

    companion object {
        const val LOAD_OPERATION = "loadComposers"

        fun newInstance() = ComposerListFragment()
    }
}
