package com.vgleadsheets.features.main.game.better

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.features.main.list.BetterLists
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class BetterGameFragment : BetterListFragment<BetterGameContent, BetterGameState>() {
    @Inject
    lateinit var viewModelFactory: BetterGameViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.LIST_GAME

    override fun getPerfSpec() = PerfSpec.GAMES

    override val viewModel: BetterGameViewModel by fragmentViewModel()

    override fun generateList(state: BetterGameState, hudState: HudState) =
        BetterLists.generateList(
            GameConfig(
                state,
                hudState,
                baseImageUrl,
                viewModel,
                perfTracker,
                getPerfSpec(),
                resources
            ),
            resources
        )

    companion object {
        const val LOAD_OPERATION = "loadGame"

        fun newInstance(idArgs: IdArgs): BetterGameFragment {
            val fragment = BetterGameFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
