package com.vgleadsheets.features.main.settings

import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject

class SettingsFragment : AsyncListFragment<SettingsData, SettingsState>() {
    @Inject
    lateinit var settingsViewModelFactory: SettingsViewModel.Factory

    override fun getTrackingScreen() = TrackingScreen.SETTINGS

    override val viewModel: SettingsViewModel by fragmentViewModel()

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        perfTracker.onTransitionStarted(getPerfScreenName())
        perfTracker.onTitleLoaded(getPerfScreenName())
    }

    override fun subscribeToViewEvents() {
        hudViewModel.alwaysShowBack()

        viewModel.selectSubscribe(SettingsState::clickedSingleTextModel) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                getFragmentRouter().showAbout()
                viewModel.clearClicked()
            }
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
