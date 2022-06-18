package com.vgleadsheets.features.main.settings.better

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.features.main.list.BetterLists
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class BetterSettingFragment : BetterListFragment<BetterSettingContent, BetterSettingState>() {
    @Inject
    lateinit var viewModelFactory: BetterSettingViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.SETTINGS

    override fun getPerfSpec() = PerfSpec.SETTINGS

    override val viewModel: BetterSettingViewModel by fragmentViewModel()

    override fun generateList(state: BetterSettingState, hudState: HudState) =
        BetterLists.generateList(
            BetterSettingConfig(
                state,
                viewModel,
                perfTracker,
                getPerfSpec(),
                resources
            ),
            resources
        )

    companion object {
        const val LOAD_OPERATION = "loadComposer"

        fun newInstance() = BetterSettingFragment()
    }
}

