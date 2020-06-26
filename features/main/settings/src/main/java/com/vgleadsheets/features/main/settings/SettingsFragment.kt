package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import javax.inject.Inject

class SettingsFragment : AsyncListFragment<SettingsData, SettingsState>() {
    @Inject
    lateinit var settingsViewModelFactory: SettingsViewModel.Factory

    override val viewModel: SettingsViewModel by fragmentViewModel()

    override fun getVglsFragmentTag() = this.javaClass.simpleName

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
