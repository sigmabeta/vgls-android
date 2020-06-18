package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.list.async.AsyncListFragment
import javax.inject.Inject

@Suppress("TooManyFunctions")
class SettingsFragment : AsyncListFragment<SettingsData, SettingsState>() {
    @Inject
    lateinit var settingsViewModelFactory: SettingsViewModel.Factory

    override val viewModel: SettingsViewModel by fragmentViewModel()

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(SettingsState::clickedSingleTextModel) {
            val clickedId = it?.dataId

            if (clickedId != null) {
                getFragmentRouter().showAbout()
                viewModel.clearClickedSingleTextModel()
            }
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
