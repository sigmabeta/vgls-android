package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.storage.Storage
import timber.log.Timber

class SettingsViewModel @AssistedInject constructor(
    @Assisted initialState: SettingsState,
    private val storage: Storage
) : MvRxViewModel<SettingsState>(initialState) {
    init {
        fetchSettings()
    }

    fun setSetting(settingId: String, newValue: Boolean) {
        // TODO These strings need to live in a common module
        val settingSaveOperation = when (settingId) {
            "SETTING_SHEET_KEEP_SCREEN_ON" -> storage.saveSettingSheetScreenOn(newValue)
            else -> throw IllegalArgumentException()
        }

        settingSaveOperation
            .subscribe(
                {
                    fetchSettings()
                },
                {
                    Timber.e("Failed to update setting: ${it.message}")
                }
            )
            .disposeOnClear()
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
        override fun create(
            viewModelContext: ViewModelContext,
            state: SettingsState
        ): SettingsViewModel? {
            val fragment: SettingsFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.settingsViewModelFactory.create(state)
        }
    }
}
