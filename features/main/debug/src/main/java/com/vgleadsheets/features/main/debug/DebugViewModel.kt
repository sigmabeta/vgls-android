package com.vgleadsheets.features.main.debug

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.storage.Storage
import com.vgleadsheets.storage.Storage.Companion.KEY_DEBUG_MISC_PERF_VIEW
import com.vgleadsheets.storage.Storage.Companion.KEY_DEBUG_NETWORK_ENDPOINT
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DebugViewModel @AssistedInject constructor(
    @Assisted initialState: DebugState,
    private val repository: VglsRepository,
    private val storage: Storage,
) : MavericksViewModel<DebugState>(initialState) {
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

    fun clearSheets() {
        suspend {
            repository.clearSheets()
        }.execute {
            copy(sheetDeletion = it)
        }
    }

    fun clearJams() {
        suspend {
            repository.clearJams()
        }.execute {
            copy(jamDeletion = it)
        }
    }

    private fun fetchSettings() = withState {
        suspend {
            storage.getAllDebugSettings()
        }.execute {
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
        when (settingId) {
            KEY_DEBUG_MISC_PERF_VIEW -> storage.saveDebugSettingPerfView(newValue)
            else -> TODO("Don't know how to save setting $settingId yet!")
        }

        fetchSettings()
    }

    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    private fun setDropdownSetting(settingId: String, newValue: Int) {
        // TODO These strings need to live in a common module
        when (settingId) {
            KEY_DEBUG_NETWORK_ENDPOINT -> storage.saveDebugSelectedNetworkEndpoint(newValue)
            else -> throw IllegalArgumentException()
        }

        fetchSettings()
        clearAll()
        setState { copy(changed = true) }
    }

    private fun clearAll() {
        suspend {
            repository.clearSheets()
            repository.clearJams()
        }.execute { this }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            initialState: DebugState,
        ): DebugViewModel
    }

    companion object : MavericksViewModelFactory<DebugViewModel, DebugState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: DebugState
        ): DebugViewModel {
            val fragment: DebugFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
