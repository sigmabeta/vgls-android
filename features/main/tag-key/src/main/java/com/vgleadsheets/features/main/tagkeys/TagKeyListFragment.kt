package com.vgleadsheets.features.main.tagkeys

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.features.main.list.BetterLists
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class TagKeyListFragment :
    BetterListFragment<TagKeyListContent, TagKeyListState>() {
    @Inject
    lateinit var viewModelFactory: TagKeyListViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.LIST_TAG_KEY

    override fun getPerfSpec() = PerfSpec.TAG_KEY

    override val viewModel: TagKeyListViewModel by fragmentViewModel()

    override fun generateList(state: TagKeyListState, hudState: HudState) =
        BetterLists.generateList(
            Config(
                state,
                Clicks(getFragmentRouter()),
                perfTracker,
                getPerfSpec(),
                resources
            ),
            resources
        )

    companion object {
        const val LOAD_OPERATION = "loadTagKeys"

        fun newInstance() = TagKeyListFragment()
    }
}
