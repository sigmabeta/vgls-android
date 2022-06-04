package com.vgleadsheets.features.main.composer.better

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

class BetterComposerFragment : BetterListFragment<BetterComposerContent, BetterComposerState>() {
    @Inject
    lateinit var viewModelFactory: BetterComposerViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.DETAIL_COMPOSER

    override fun getPerfSpec() = PerfSpec.COMPOSER

    override val viewModel: BetterComposerViewModel by fragmentViewModel()

    override fun generateList(state: BetterComposerState, hudState: HudState) =
        BetterLists.generateList(
            BetterComposerConfig(
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
        const val LOAD_OPERATION = "loadComposer"

        fun newInstance(idArgs: IdArgs): BetterComposerFragment {
            val fragment = BetterComposerFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}

