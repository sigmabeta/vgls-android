package com.vgleadsheets.features.main.tagsongs.better

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

class BetterTagValueSongFragment :
    BetterListFragment<BetterTagValueSongContent, BetterTagValueSongState>() {
    @Inject
    lateinit var viewModelFactory: BetterTagValueSongViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.LIST_TAG_VALUE_SONG

    override fun getPerfSpec() = PerfSpec.TAG_SONGS

    override val viewModel: BetterTagValueSongViewModel by fragmentViewModel()

    override fun generateList(state: BetterTagValueSongState, hudState: HudState) =
        BetterLists.generateList(
            BetterTagValueSongConfig(
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
        const val LOAD_OPERATION = "loadTagValue"

        fun newInstance(idArgs: IdArgs): BetterTagValueSongFragment {
            val fragment = BetterTagValueSongFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}

