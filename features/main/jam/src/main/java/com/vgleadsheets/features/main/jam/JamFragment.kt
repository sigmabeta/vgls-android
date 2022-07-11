package com.vgleadsheets.features.main.jam

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class JamFragment : BetterListFragment<JamContent, JamState>() {
    @Inject
    lateinit var viewModelFactory: JamViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.DETAIL_JAM

    override fun getPerfSpec() = PerfSpec.JAM

    override val viewModel: JamViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Quit the screen if the jam is deleted.
        viewModel.asyncSubscribe(
            JamState::deletion,
            deliveryMode = uniqueOnly("deletion")
        ) {
            activity?.onBackPressed()
        }
    }

    override fun generateListConfig(state: JamState, hudState: HudState) = Config(
        state,
        hudState,
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
        const val LOAD_OPERATION = "loadJam"

        fun newInstance(idArgs: IdArgs): JamFragment {
            val fragment = JamFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
