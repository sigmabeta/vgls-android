package com.vgleadsheets.features.main.sheet

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
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

    override fun invalidate() {
        withState(viewModel) {
            val song = it.contentLoad.song()
            if (song != null) {
                hudViewModel.setSelectedSong(song)
            }
        }
        super.invalidate()
    }

    override fun generateListConfig(state: SongState, hudState: HudState) = Config(
        state,
        hudState,
        baseImageUrl,
        Clicks(
            getFragmentRouter()
        ),
        perfTracker,
        getPerfSpec(),
        resources
    )

    override fun onStop() {
        super.onStop()
        hudViewModel.clearSelectedSong()
    }

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
