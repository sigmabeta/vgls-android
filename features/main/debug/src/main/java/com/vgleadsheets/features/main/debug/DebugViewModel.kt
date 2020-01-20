package com.vgleadsheets.features.main.debug

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.storage.Storage
import io.reactivex.Completable
import timber.log.Timber

class DebugViewModel @AssistedInject constructor(
    @Assisted initialState: DebugState,
    private val storage: Storage,
    private val repository: Repository
) : MvRxViewModel<DebugState>(initialState) {
    init {
        fetchDebugSettings()
    }

    fun clearSheets() {
        repository.clearSheets()
            .execute {
                copy(sheetDeletion = it)
            }
    }

    fun clearJams() {
        repository.clearJams()
            .execute {
                copy(jamDeletion = it)
            }
    }

    fun setDropdownSetting(settingId: String, newValue: Int) {
        // TODO These strings need to live in a common module
        val settingSaveOperation = when (settingId) {
            "DEBUG_NETWORK_ENDPOINT" -> storage.saveSelectedNetworkEndpoint(newValue)
            else -> throw IllegalArgumentException()
        }

        settingSaveOperation
            .subscribe(
                {
                    fetchDebugSettings()
                    clearAll()
                    setState { copy(changed = true) }
                },
                {
                    Timber.e("Failed to update setting: ${it.message}")
                }
            )
            .disposeOnClear()
    }

    private fun fetchDebugSettings() {
        storage.getAllDebugSettings()
            .execute {
                copy(settings = it)
            }
    }

    private fun clearAll() {
        Completable
            .merge(
                listOf(
                    repository.clearSheets(),
                    repository.clearJams()
                )
            )
            .subscribe()
            .disposeOnClear()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: DebugState): DebugViewModel
    }

    companion object : MvRxViewModelFactory<DebugViewModel, DebugState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: DebugState
        ): DebugViewModel? {
            val fragment: DebugFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.debugViewModelFactory.create(state)
        }
    }
}
