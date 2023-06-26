package com.vgleadsheets.features.main.tagkeys

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.ComposeListFragment
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class TagKeyListFragment :
    ComposeListFragment<TagKeyListState>() {
    @Inject
    lateinit var viewModelFactory: TagKeyListViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.LIST_TAG_KEY

    override fun getPerfSpec() = PerfSpec.TAG_KEY

    override val alwaysShowBack = false

    override val viewModel: TagKeyListViewModel by fragmentViewModel()

    override fun generateListConfig(state: TagKeyListState, hudState: HudState) = Config(
        state,
        Clicks(getFragmentRouter()),
        perfTracker,
        getPerfSpec(),
        resources
    )

    companion object {
        const val LOAD_OPERATION = "loadTagKeys"

        fun newInstance() = TagKeyListFragment()
    }
}
