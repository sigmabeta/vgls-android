package com.vgleadsheets.features.main.songs.better

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.features.main.list.BetterLists
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class BetterSongListFragment :
    BetterListFragment<BetterSongListContent, BetterSongListState>() {
    @Inject
    lateinit var viewModelFactory: BetterSongListViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.LIST_SHEET

    override fun getPerfSpec() = PerfSpec.SONGS

    override val viewModel: BetterSongListViewModel by fragmentViewModel()

    override fun generateList(state: BetterSongListState, hudState: HudState) =
        BetterLists.generateList(
            BetterSongListConfig(
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
        const val LOAD_OPERATION = "loadSongs"

        fun newInstance() = BetterSongListFragment()
    }
}
