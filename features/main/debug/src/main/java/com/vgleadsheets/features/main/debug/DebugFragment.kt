package com.vgleadsheets.features.main.debug

import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import javax.inject.Inject

class DebugFragment : AsyncListFragment<DebugData, DebugState>() {
    @Inject
    lateinit var debugViewModelFactory: DebugViewModel.Factory

    override val viewModel: DebugViewModel by fragmentViewModel()

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    override fun subscribeToViewEvents() {
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

    override fun onBackPress() = withState(viewModel) {
        if (it.changed) {
            getFragmentRouter().restartApp()
            return@withState true
        }
        return@withState false
    }

    companion object {
        fun newInstance() = DebugFragment()
    }
}

