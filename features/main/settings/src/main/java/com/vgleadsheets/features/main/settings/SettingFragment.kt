package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class SettingFragment : BetterListFragment<SettingContent, SettingState>() {
    @Inject
    lateinit var viewModelFactory: SettingViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.SETTINGS

    override fun getPerfSpec() = PerfSpec.SETTINGS

    override val viewModel: SettingViewModel by fragmentViewModel()

    override fun generateListConfig(state: SettingState, hudState: HudState) = Config(
        state,
        Clicks(
            viewModel,
            getFragmentRouter()
        ),
        perfTracker,
        getPerfSpec(),
        resources
    )

    companion object {
        const val LOAD_OPERATION = "loadComposer"

        fun newInstance() = SettingFragment()
    }
}
