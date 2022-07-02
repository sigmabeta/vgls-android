package com.vgleadsheets.features.main.game

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

class GameFragment : BetterListFragment<Content, GameState>() {
    @Inject
    lateinit var viewModelFactory: GameViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.DETAIL_GAME

    override fun getPerfSpec() = PerfSpec.GAME

    override val viewModel: GameViewModel by fragmentViewModel()

    override fun generateList(state: GameState, hudState: HudState) =
        BetterLists.generateList(
            Config(
                state,
                hudState,
                baseImageUrl,
                Clicks(
                    getFragmentRouter(),
                ),
                perfTracker,
                getPerfSpec(),
                resources
            ),
            resources
        )

    companion object {
        const val LOAD_OPERATION = "loadGame"

        fun newInstance(idArgs: IdArgs): GameFragment {
            val fragment = GameFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
