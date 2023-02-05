package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.repository.DelayOrErrorRepository
import com.vgleadsheets.storage.Storage
import com.vgleadsheets.storage.Storage.Companion.KEY_DEBUG_NETWORK_ENDPOINT
import com.vgleadsheets.storage.Storage.Companion.KEY_SHEETS_KEEP_SCREEN_ON
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlin.random.Random

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
        when (settingId) {
            KEY_SHEETS_KEEP_SCREEN_ON -> storage.saveSettingSheetScreenOn(newValue)
            else -> throw IllegalArgumentException()
        }
    }

    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    fun setDropdownSetting(settingId: String, newValue: Int) {
        // TODO These strings need to live in a common module
        when (settingId) {
            KEY_DEBUG_NETWORK_ENDPOINT -> storage.saveDebugSelectedNetworkEndpoint(newValue)
            else -> throw IllegalArgumentException()
        }
    }

    private fun fetchSettings() = withState {
        suspend {
            delay(
                DelayOrErrorRepository.DELAY_MINIMUM_MS + Random.nextLong(DelayOrErrorRepository.DELAY_VARIANCE_MS),
            )
            storage.getAllSettings()
        }.execute {
            copy(
                contentLoad = contentLoad.copy(
                    settings = it
                )
            )
        }
    }

    @AssistedFactory
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
