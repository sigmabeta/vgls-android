package com.vgleadsheets.features.main.composers

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.ComposeListFragment
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class ComposerListFragment : ComposeListFragment<ComposerListState>() {
    @Inject
    lateinit var viewModelFactory: ComposerListViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.LIST_COMPOSER

    override fun getPerfSpec() = PerfSpec.COMPOSERS

    override val alwaysShowBack = false

    override val viewModel: ComposerListViewModel by fragmentViewModel()

    override fun generateListConfig(state: ComposerListState, hudState: HudState) = Config(
        state,
        Clicks(
            getFragmentRouter(),
            hudViewModel
        ),
        perfTracker,
        getPerfSpec(),
        resources
    )

    companion object {
        const val LOAD_OPERATION = "loadComposers"

        fun newInstance() = ComposerListFragment()
    }
}
