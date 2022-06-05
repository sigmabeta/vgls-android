package com.vgleadsheets.features.main.tagkeys.better

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.features.main.list.BetterLists
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class BetterTagKeyListFragment :
    BetterListFragment<BetterTagKeyListContent, BetterTagKeyListState>() {
    @Inject
    lateinit var viewModelFactory: BetterTagKeyListViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.LIST_TAG_KEY

    override fun getPerfSpec() = PerfSpec.TAG_KEY

    override val viewModel: BetterTagKeyListViewModel by fragmentViewModel()

    override fun generateList(state: BetterTagKeyListState, hudState: HudState) =
        BetterLists.generateList(
            BetterTagKeyListConfig(
                state,
                hudState,
                viewModel,
                BetterTagKeyListClicks,
                perfTracker,
                getPerfSpec(),
                resources
            ),
            resources
        )

    companion object {
        const val LOAD_OPERATION = "loadTagKeys"

        fun newInstance() = BetterTagKeyListFragment()
    }
}
