package com.vgleadsheets.features.main.sheet

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

class SongFragment : BetterListFragment<SongContent, SongState>() {
    @Inject
    lateinit var viewModelFactory: SongViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.DETAIL_SHEET

    override fun getPerfSpec() = PerfSpec.SHEET

    override val viewModel: SongViewModel by fragmentViewModel()

    override fun generateList(state: SongState, hudState: HudState) =
        BetterLists.generateList(
            Config(
                state,
                hudState,
                baseImageUrl,
                Clicks(getFragmentRouter()),
                perfTracker,
                getPerfSpec(),
                resources
            ),
            resources
        )

    companion object {
        const val LOAD_OPERATION = "loadSong"

        fun newInstance(idArgs: IdArgs): SongFragment {
            val fragment = SongFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}

