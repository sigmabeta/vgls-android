package com.vgleadsheets.features.main.settings.better

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.features.main.debug.R
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListFragment
import com.vgleadsheets.features.main.list.BetterLists
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

class BetterDebugFragment : BetterListFragment<BetterDebugContent, BetterDebugState>() {
    @Inject
    lateinit var viewModelFactory: BetterDebugViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    override fun getTrackingScreen() = TrackingScreen.DEBUG

    override fun getPerfSpec() = PerfSpec.DEBUG

    override val viewModel: BetterDebugViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectSubscribe(
            BetterDebugState::changed,
            deliveryMode = uniqueOnly("changed")
        ) { changed ->
            if (changed) {
                showSnackbar(getString(R.string.snack_restart_on_exit))
            }
        }

        viewModel.asyncSubscribe(
            BetterDebugState::jamDeletion,
            deliveryMode = uniqueOnly("jamDeletion")
        ) {
            showSnackbar("Jams cleared.")
        }

        viewModel.asyncSubscribe(
            BetterDebugState::sheetDeletion,
            deliveryMode = uniqueOnly("sheetDeletion")
        ) {
            showSnackbar("Sheets cleared.")
        }
    }

    override fun generateList(state: BetterDebugState, hudState: HudState) =
        BetterLists.generateList(
            BetterDebugConfig(
                state,
                viewModel,
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
        const val LOAD_OPERATION = "loadComposer"

        fun newInstance() = BetterDebugFragment()
    }
}

