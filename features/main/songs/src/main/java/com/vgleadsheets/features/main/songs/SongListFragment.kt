package com.vgleadsheets.features.main.songs

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.ViewListFragment
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class SongListFragment :
    ViewListFragment<SongListContent, SongListState>() {
    @Inject
    lateinit var viewModelFactory: SongListViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.LIST_SHEET

    override fun getPerfSpec() = PerfSpec.SONGS

    override val alwaysShowBack = false

    override val viewModel: SongListViewModel by fragmentViewModel()

    override fun generateListConfig(state: SongListState, hudState: HudState) = Config(
        state,
        hudState,
        baseImageUrl,
        Clicks(getFragmentRouter()),
        perfTracker,
        getPerfSpec(),
        resources
    )

    companion object {
        const val LOAD_OPERATION = "loadSongs"

        fun newInstance() = SongListFragment()
    }
}
