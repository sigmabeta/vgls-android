package com.vgleadsheets.features.main.favorites

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class FavoriteListFragment :
    BetterListFragment<FavoriteListContent, FavoriteListState>() {
    @Inject
    lateinit var viewModelFactory: FavoriteListViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.LIST_FAVORITE

    override fun getPerfSpec() = PerfSpec.FAVORITE

    override val alwaysShowBack = false

    override val viewModel: FavoriteListViewModel by fragmentViewModel()

    override fun generateListConfig(state: FavoriteListState, hudState: HudState) = Config(
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
        const val LOAD_OPERATION = "loadFavorites"

        fun newInstance() = FavoriteListFragment()
    }
}
