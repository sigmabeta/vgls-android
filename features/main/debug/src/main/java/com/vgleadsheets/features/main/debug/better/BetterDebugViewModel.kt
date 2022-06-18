package com.vgleadsheets.features.main.settings.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.storage.Storage
import com.vgleadsheets.storage.Storage.Companion.KEY_DEBUG_MISC_PERF_VIEW
import com.vgleadsheets.storage.Storage.Companion.KEY_DEBUG_NETWORK_ENDPOINT
import io.reactivex.Completable
import timber.log.Timber

class BetterDebugViewModel @AssistedInject constructor(
    @Assisted initialState: BetterDebugState,
    @Assisted private val router: FragmentRouter,
    private val repository: Repository,
    private val storage: Storage,
) : MvRxViewModel<BetterDebugState>(initialState) {
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

    private fun fetchSettings() = withState {
        storage.getAllDebugSettings()
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
            KEY_DEBUG_MISC_PERF_VIEW -> storage.saveDebugSettingPerfView(newValue)
            else -> TODO("Don't know how to save setting $settingId yet!")
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
                    clearAll()
                    setState { copy(changed = true) }
                },
                {
                    Timber.e("Failed to update setting: ${it.message}")
                }
            )
            .disposeOnClear()
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
        fun create(
            initialState: BetterDebugState,
            router: FragmentRouter
        ): BetterDebugViewModel
    }

    companion object : MvRxViewModelFactory<BetterDebugViewModel, BetterDebugState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterDebugState
        ): BetterDebugViewModel {
            val fragment: BetterDebugFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state, fragment.activity as FragmentRouter)
        }
    }
}
