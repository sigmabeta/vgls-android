package com.vgleadsheets.features.main.games

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.features.main.list.BetterLists
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class GameListFragment : BetterListFragment<GameListContent, GameListState>() {
    @Inject
    lateinit var viewModelFactory: GameListViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.LIST_GAME

    override fun getPerfSpec() = PerfSpec.GAMES

    override val viewModel: GameListViewModel by fragmentViewModel()

    override fun generateList(state: GameListState, hudState: HudState) =
        BetterLists.generateList(
            Config(
                state,
                hudState,
                Clicks(getFragmentRouter()),
                perfTracker,
                getPerfSpec(),
                resources
            ),
            resources
        )

    companion object {
        const val LOAD_OPERATION = "loadGames"

        fun newInstance() = GameListFragment()
    }
}
