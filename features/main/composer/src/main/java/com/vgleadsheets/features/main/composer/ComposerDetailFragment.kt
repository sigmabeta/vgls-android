package com.vgleadsheets.features.main.composer

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class ComposerDetailFragment : BetterListFragment<ComposerDetailContent, ComposerDetailState>() {
    @Inject
    lateinit var viewModelFactory: ComposerDetailViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.DETAIL_COMPOSER

    override fun getPerfSpec() = PerfSpec.COMPOSER

    override val viewModel: ComposerDetailViewModel by fragmentViewModel()

    override fun generateListConfig(state: ComposerDetailState, hudState: HudState) = Config(
        state,
        hudState,
        baseImageUrl,
        Clicks(
            getFragmentRouter(),
            hudViewModel
        ),
        perfTracker,
        getPerfSpec(),
        resources
    )

    companion object {
        const val LOAD_OPERATION = "loadComposer"

        fun newInstance(idArgs: IdArgs): ComposerDetailFragment {
            val fragment = ComposerDetailFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
