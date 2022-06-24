package com.vgleadsheets.features.main.settings.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.storage.Storage
import com.vgleadsheets.storage.Storage.Companion.KEY_DEBUG_NETWORK_ENDPOINT
import com.vgleadsheets.storage.Storage.Companion.KEY_SHEETS_KEEP_SCREEN_ON
import timber.log.Timber

class BetterSettingViewModel @AssistedInject constructor(
    @Assisted initialState: BetterSettingState,
    private val storage: Storage,
) : MvRxViewModel<BetterSettingState>(initialState) {
    init {
        fetchSettings()
    }

    fun onBooleanSettingClicked(
        id: String,
        newSetting: Boolean
    ) {
        setBooleanSetting(id, newSetting)
    }

    fun onDropdownSettingSelected(id: String, selectedPosition: Int) {
        setDropdownSetting(id, selectedPosition)
    }

    fun onAboutClicked() {
        router.showAbout()
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

    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    private fun setBooleanSetting(settingId: String, newValue: Boolean) {
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
    private fun setDropdownSetting(settingId: String, newValue: Int) {
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

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterSettingState,
        ): BetterSettingViewModel
    }

    companion object : MvRxViewModelFactory<BetterSettingViewModel, BetterSettingState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterSettingState
        ): BetterSettingViewModel {
            val fragment: BetterSettingFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
