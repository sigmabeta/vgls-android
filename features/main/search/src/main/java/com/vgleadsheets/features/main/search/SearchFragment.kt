package com.vgleadsheets.features.main.search

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.UniqueOnly
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class SearchFragment : BetterListFragment<SearchContent, SearchState>() {
    @Inject
    lateinit var viewModelFactory: SearchViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.SEARCH

    override fun getPerfSpec() = PerfSpec.SEARCH

    override fun disablePerfTracking() = true

    override val viewModel: SearchViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hudViewModel.selectSubscribe(
            HudState::searchQuery,
            deliveryMode = UniqueOnly("query")
        ) {
            if (it != null) {
                if (it.lowercase().contains("stickerbr")) {
                    onStickerBrEntered(it)
                } else {
                    onSearchQueryEntered(it)
                }
            } else {
                viewModel.onQueryClear()
            }
        }
    }

    override fun generateListConfig(state: SearchState, hudState: HudState) = Config(
        state,
        hudState,
        baseImageUrl,
        Clicks(
            getFragmentRouter(),
            hudViewModel
        ),
        resources
    )

    private fun onSearchQueryEntered(query: String) {
        viewModel.startQuery(query)
    }

    private fun onStickerBrEntered(query: String) {
        tracker.logStickerBr()
        viewModel.showStickerBr(query)
    }

    companion object {
        const val LOAD_OPERATION = "loadSearch"

        fun newInstance() = SearchFragment()
    }
}
