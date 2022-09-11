package com.vgleadsheets.features.main.search

import android.os.Bundle
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.args.NullableStringArgs
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

    fun startQuery(query: String?) {
        query ?: return
        viewModel.startQuery(query)
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

    companion object {
        const val LOAD_OPERATION = "loadSearch"

        fun newInstance(stringArgs: NullableStringArgs): SearchFragment {
            val fragment = SearchFragment()

            val args = Bundle()
            args.putParcelable(Mavericks.KEY_ARG, stringArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
