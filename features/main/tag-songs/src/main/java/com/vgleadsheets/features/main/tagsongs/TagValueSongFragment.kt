package com.vgleadsheets.features.main.tagsongs

import android.os.Bundle
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class TagValueSongFragment :
    BetterListFragment<TagValueSongContent, TagValueSongState>() {
    @Inject
    lateinit var viewModelFactory: TagValueSongViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.LIST_TAG_VALUE_SONG

    override fun getPerfSpec() = PerfSpec.TAG_SONGS

    override val viewModel: TagValueSongViewModel by fragmentViewModel()

    override fun generateListConfig(state: TagValueSongState, hudState: HudState) =
        Config(
            state,
            hudState,
            baseImageUrl,
            Clicks(getFragmentRouter()),
            perfTracker,
            getPerfSpec(),
            resources
        )

    companion object {
        const val LOAD_OPERATION = "loadTagValue"

        fun newInstance(idArgs: IdArgs): TagValueSongFragment {
            val fragment = TagValueSongFragment()

            val args = Bundle()
            args.putParcelable(Mavericks.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
