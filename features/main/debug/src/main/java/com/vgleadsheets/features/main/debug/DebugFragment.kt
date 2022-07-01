package com.vgleadsheets.features.main.debug

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.features.main.list.BetterLists
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class DebugFragment : BetterListFragment<DebugContent, DebugState>() {
    @Inject
    lateinit var viewModelFactory: DebugViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.DEBUG

    override fun getPerfSpec() = PerfSpec.DEBUG

    override val viewModel: DebugViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectSubscribe(
            DebugState::changed,
            deliveryMode = uniqueOnly("changed")
        ) { changed ->
            if (changed) {
                showSnackbar(getString(R.string.snack_restart_on_exit))
            }
        }

        viewModel.asyncSubscribe(
            DebugState::jamDeletion,
            deliveryMode = uniqueOnly("jamDeletion")
        ) {
            showSnackbar("Jams cleared.")
        }

        viewModel.asyncSubscribe(
            DebugState::sheetDeletion,
            deliveryMode = uniqueOnly("sheetDeletion")
        ) {
            showSnackbar("Sheets cleared.")
        }
    }

    override fun generateList(state: DebugState, hudState: HudState) =
        BetterLists.generateList(
            Config(
                state,
                Clicks(viewModel),
                perfTracker,
                getPerfSpec(),
                resources
            ),
            resources
        )

    override fun onBackPress() = withState(viewModel) {
        if (it.changed) {
            getFragmentRouter().restartApp()
            return@withState true
        }
        return@withState false
    }

    companion object {
        const val LOAD_OPERATION = "loadDebug"

        fun newInstance() = DebugFragment()
    }
}
