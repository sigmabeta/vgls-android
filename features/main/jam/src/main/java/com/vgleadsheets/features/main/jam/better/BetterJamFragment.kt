package com.vgleadsheets.features.main.jam.better

import android.os.Bundle
import android.view.View
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

class BetterJamFragment : BetterListFragment<BetterJamContent, BetterJamState>() {
    @Inject
    lateinit var viewModelFactory: BetterJamViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.DETAIL_JAM

    override fun getPerfSpec() = PerfSpec.JAM

    override val viewModel: BetterJamViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Quit the screen if the jam is deleted.
        viewModel.asyncSubscribe(
            BetterJamState::deletion,
            deliveryMode = uniqueOnly("deletion")
        ) {
            activity?.onBackPressed()
        }
    }

    override fun generateList(state: BetterJamState, hudState: HudState) =
        BetterLists.generateList(
            BetterJamConfig(
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
        const val LOAD_OPERATION = "loadJam"

        fun newInstance(idArgs: IdArgs): BetterJamFragment {
            val fragment = BetterJamFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}

