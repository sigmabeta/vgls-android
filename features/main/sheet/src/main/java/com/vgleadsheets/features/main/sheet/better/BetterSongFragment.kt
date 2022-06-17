package com.vgleadsheets.features.main.sheet.better

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.features.main.list.BetterLists
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class BetterSongFragment : BetterListFragment<BetterSongContent, BetterSongState>() {
    @Inject
    lateinit var viewModelFactory: BetterSongViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.DETAIL_SHEET

    override fun getPerfSpec() = PerfSpec.SHEET

    override val viewModel: BetterSongViewModel by fragmentViewModel()

    override fun generateList(state: BetterSongState, hudState: HudState) =
        BetterLists.generateList(
            BetterSongConfig(
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
        const val LOAD_OPERATION = "loadSong"

        fun newInstance(idArgs: IdArgs): BetterSongFragment {
            val fragment = BetterSongFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}

