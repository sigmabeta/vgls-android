package com.vgleadsheets.features.main.game

import android.os.Bundle
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.detail.DetailFragment
import com.vgleadsheets.nav.NavState
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class GameDetailFragment : DetailFragment<GameDetailState>() {
    @Inject
    lateinit var viewModelFactory: GameDetailViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.DETAIL_GAME

    override fun getPerfSpec() = PerfSpec.GAME

    override val viewModel: GameDetailViewModel by fragmentViewModel()

    override fun generateListConfig(state: GameDetailState, navState: NavState) = Config(
        state,
        navState,
        baseImageUrl,
        Clicks(
            getFragmentRouter(),
            viewModel
        ),
        perfTracker,
        getPerfSpec(),
        resources
    )

    companion object {
        const val LOAD_OPERATION = "loadGame"

        fun newInstance(idArgs: IdArgs): GameDetailFragment {
            val fragment = GameDetailFragment()

            val args = Bundle()
            args.putParcelable(Mavericks.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
