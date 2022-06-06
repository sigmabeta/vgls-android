package com.vgleadsheets.features.main.tagvalues.better

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

class BetterTagValueFragment : BetterListFragment<BetterTagValueContent, BetterTagValueState>() {
    @Inject
    lateinit var viewModelFactory: BetterTagValueViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.LIST_TAG_VALUE

    override fun getPerfSpec() = PerfSpec.TAG_VALUE

    override val viewModel: BetterTagValueViewModel by fragmentViewModel()

    override fun generateList(state: BetterTagValueState, hudState: HudState) =
        BetterLists.generateList(
            BetterTagValueConfig(
                state,
                hudState,
                viewModel,
                perfTracker,
                getPerfSpec(),
                resources
            ),
            resources
        )

    companion object {
        const val LOAD_OPERATION = "loadTagValue"

        fun newInstance(idArgs: IdArgs): BetterTagValueFragment {
            val fragment = BetterTagValueFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}

