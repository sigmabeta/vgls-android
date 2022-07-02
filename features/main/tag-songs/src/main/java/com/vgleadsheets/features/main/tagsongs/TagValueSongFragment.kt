package com.vgleadsheets.features.main.tagsongs

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

class TagValueSongFragment :
    BetterListFragment<Content, TagValueSongState>() {
    @Inject
    lateinit var viewModelFactory: TagValueSongViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.LIST_TAG_VALUE_SONG

    override fun getPerfSpec() = PerfSpec.TAG_SONGS

    override val viewModel: TagValueSongViewModel by fragmentViewModel()

    override fun generateList(state: TagValueSongState, hudState: HudState) =
        BetterLists.generateList(
            TagValueSongConfig(
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
        const val LOAD_OPERATION = "loadTagValue"

        fun newInstance(idArgs: IdArgs): TagValueSongFragment {
            val fragment = TagValueSongFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
