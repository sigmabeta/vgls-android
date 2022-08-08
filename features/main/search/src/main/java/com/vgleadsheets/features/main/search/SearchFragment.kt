package com.vgleadsheets.features.main.search

import android.os.Bundle
import com.airbnb.mvrx.MvRx
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

    private var stickerBrLogged = false

    // {
    //     if (it != null) {
    //         if (it.lowercase().contains("stickerbr")) {
    //                     viewModel.startQuery(query)
    //         } else {
    //             onSearchQueryEntered(it)
    //         }
    //     } else {
    //         viewModel.clearQuery()
    //     }
    // }

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

    private fun onStickerBrEntered() {
        if (!stickerBrLogged) {
            tracker.logStickerBr()
            stickerBrLogged = true
        }
    }

    companion object {
        const val LOAD_OPERATION = "loadSearch"

        fun newInstance(stringArgs: NullableStringArgs): SearchFragment {
            val fragment = SearchFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, stringArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
