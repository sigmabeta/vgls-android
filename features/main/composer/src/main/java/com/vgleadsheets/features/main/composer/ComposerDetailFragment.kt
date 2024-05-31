package com.vgleadsheets.features.main.composer

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

class ComposerDetailFragment : DetailFragment<ComposerDetailState>() {
    @Inject
    lateinit var viewModelFactory: ComposerDetailViewModel.Factory

    @Inject
    @Named("VglssourceInfo")
    lateinit var basesourceInfo: String

    override fun getTrackingScreen() = TrackingScreen.DETAIL_COMPOSER

    override fun getPerfSpec() = PerfSpec.COMPOSER

    override val viewModel: ComposerDetailViewModel by fragmentViewModel()

    override fun generateListConfig(state: ComposerDetailState, navState: NavState) = Config(
        state,
        navState,
        basesourceInfo,
        Clicks(
            getFragmentRouter(),
            viewModel
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
            args.putParcelable(Mavericks.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
