package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MavericksViewModel
import com.vgleadsheets.storage.Storage
import com.vgleadsheets.storage.Storage.Companion.KEY_DEBUG_NETWORK_ENDPOINT
import com.vgleadsheets.storage.Storage.Companion.KEY_SHEETS_KEEP_SCREEN_ON
import timber.log.Timber

class SettingViewModel @AssistedInject constructor(
    @Assisted initialState: SettingState,
    private val storage: Storage,
) : MavericksViewModel<SettingState>(initialState) {
    init {
        fetchSettings()
    }

    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    fun setBooleanSetting(settingId: String, newValue: Boolean) {
        // TODO These strings need to live in a common module
        val settingSaveOperation = when (settingId) {
            KEY_SHEETS_KEEP_SCREEN_ON -> storage.saveSettingSheetScreenOn(newValue)
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

    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    fun setDropdownSetting(settingId: String, newValue: Int) {
        // TODO These strings need to live in a common module
        val settingSaveOperation = when (settingId) {
            KEY_DEBUG_NETWORK_ENDPOINT -> storage.saveDebugSelectedNetworkEndpoint(newValue)
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

    private fun fetchSettings() = withState {
        storage.getAllSettings()
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        settings = it
                    )
                )
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: SettingState,
        ): SettingViewModel
    }

    companion object : MavericksViewModelFactory<SettingViewModel, SettingState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SettingState
        ): SettingViewModel {
            val fragment: SettingFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
