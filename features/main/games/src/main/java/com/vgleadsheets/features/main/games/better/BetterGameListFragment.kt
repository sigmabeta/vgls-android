package com.vgleadsheets.features.main.games.better

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.features.main.list.BetterLists
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class BetterGameListFragment : BetterListFragment<BetterGameListContent, BetterGameListState>() {
    @Inject
    lateinit var viewModelFactory: BetterGameListViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.LIST_GAME

    override fun getPerfSpec() = PerfSpec.GAMES

    override val viewModel: BetterGameListViewModel by fragmentViewModel()

    override fun generateList(state: BetterGameListState, hudState: HudState) =
        BetterLists.generateList(
            BetterGameListConfig(
                state,
                hudState,
                viewModel,
                BetterGameListClicks,
                perfTracker,
                getPerfSpec(),
                resources
            ),
            resources
        )

    companion object {
        const val LOAD_OPERATION = "loadGames"

        fun newInstance() = BetterGameListFragment()
    }
}
