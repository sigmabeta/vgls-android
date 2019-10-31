package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.storage.Storage

class SettingsViewModel @AssistedInject constructor(
    @Assisted initialState: SettingsState,
    private val storage: Storage
) : MvRxViewModel<SettingsState>(initialState) {
    init {
        fetchSettings()
    }

    private fun fetchSettings() {
        storage.getAllSettings()
            .execute {
                copy(settings = it)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SettingsState): SettingsViewModel
    }

    companion object : MvRxViewModelFactory<SettingsViewModel, SettingsState> {
        override fun create(viewModelContext: ViewModelContext, state: SettingsState): SettingsViewModel? {
            val fragment: SettingsFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.settingsViewModelFactory.create(state)
        }
    }
}
