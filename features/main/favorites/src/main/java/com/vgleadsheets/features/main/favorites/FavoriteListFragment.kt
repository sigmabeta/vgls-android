package com.vgleadsheets.features.main.favorites

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.list.ComposeListFragment
import com.vgleadsheets.nav.NavState
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class FavoriteListFragment :
    ComposeListFragment<FavoriteListState>() {
    @Inject
    lateinit var viewModelFactory: FavoriteListViewModel.Factory

    @Inject
    @Named("VglssourceInfo")
    lateinit var basesourceInfo: String

    override fun getTrackingScreen() = TrackingScreen.LIST_FAVORITE

    override fun getPerfSpec() = PerfSpec.FAVORITE

    override val alwaysShowBack = false

    override val viewModel: FavoriteListViewModel by fragmentViewModel()

    override fun generateListConfig(state: FavoriteListState, navState: NavState) = Config(
        state,
        navState,
        basesourceInfo,
        Clicks(
            getFragmentRouter(),
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
