package com.vgleadsheets.features.main.hud

import androidx.fragment.app.FragmentActivity
import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.storage.Storage
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Suppress("TooManyFunctions")
class HudViewModel @AssistedInject constructor(
    @Assisted initialState: HudState,
    private val repository: Repository,
    private val storage: Storage
) : MvRxViewModel<HudState>(initialState) {
    private var timer: Disposable? = null

    private var jamDisposables = CompositeDisposable()

    init {
        checkSavedPartSelection()
        checkLastUpdateTime()
        checkForUpdate()
    }

    fun setActiveJam(jamId: Long) = setState { copy(activeJamId = jamId) }

    fun cancelJam(reason: String) {
        jamDisposables.clear()
        setState {
            copy(
                jamCancellationReason = reason,
                activeJamSheetId = null,
                activeJamId = null
            )
        }
    }

    fun followJam(jamId: Long) {
        repository.getJam(jamId)
            .subscribe(
                {
                    subscribeToJamNetwork(it)
                },
                {
                    val message = "Failed to get Jam from database: ${it.message}"
                    Timber.e(message)
                    cancelJam(message)
                }
            )
            .disposeOnClear()

        subscribeToJamDatabase(jamId)
    }

    fun alwaysShowBack() = setState { copy(alwaysShowBack = true) }

    fun dontAlwaysShowBack() = setState { copy(alwaysShowBack = false) }

    fun onMenuClick() = withState {
        if (it.menuExpanded) {
            hideMenu()
        } else {
            showMenu()
        }
    }

    fun onMenuBackPress() {
        hideMenu()
    }

    fun onMenuAction() {
        setState { copy(menuExpanded = false) }
    }

    fun setAvailableParts(parts: List<Part>) = withState {
        Timber.i("Setting available parts: $parts")
        val selectedId = it.parts?.first { part -> part.selected }?.apiId
        setState {
            copy(parts = PartSelectorItem.getAvailablePartPickerItems(parts, selectedId))
        }
    }

    fun resetAvailableParts() = withState {
        val selectedId = it.parts?.first { part -> part.selected }?.apiId
        setState {
            copy(parts = PartSelectorItem.getDefaultPartPickerItems(selectedId))
        }
    }

    fun onPartSelect(apiId: String) = withState { state ->
        setState {
            copy(parts = setSelection(apiId, state.parts), menuExpanded = false)
        }
    }

    fun refresh() = withState {
        if (it.digest !is Loading) {
            repository.forceRefresh()
                .execute { digest ->
                    copy(digest = digest)
                }
        }
    }

    fun clearDigest() = setState { copy(digest = Uninitialized) }

    fun searchClick() = withState { state ->
        if (!state.searchVisible) {
            setState { copy(searchVisible = true) }
        }
    }

    fun searchQuery(query: String?) = withState { state ->
        if (state.searchVisible) {
            setState { copy(searchQuery = query) }
        }
    }

    fun exitSearch() = withState { state ->
        if (state.searchVisible) {
            setState { copy(searchVisible = false, searchQuery = null) }
        }
    }

    fun hideHud() = withState { state ->
        if (state.hudVisible) {
            setState { copy(hudVisible = false) }
        }
    }

    fun showHud() = withState { state ->
        stopTimer()
        if (!state.hudVisible) {
            setState { copy(hudVisible = true) }
        }
    }

    fun startHudTimer() = withState { state ->
        stopTimer()
        if (!state.menuExpanded) {
            timer = Observable.timer(TIMEOUT_HUD_VISIBLE, TimeUnit.MILLISECONDS)
                .execute { timer ->
                    if (timer is Success) {
                        copy(hudVisible = false)
                    } else {
                        this
                    }
                }
        }
    }

    fun stopHudTimer() {
        stopTimer()
    }

    fun onRandomSelectClick(selectedPart: PartSelectorItem) {
        repository
            .getAllSongs()
            .firstOrError()
            .map { songs ->
                songs.filter { song ->
                    song.parts?.firstOrNull { part -> part.name == selectedPart.apiId } != null
                }
            }
            .map { it.random() }
            .execute {
                copy(random = it)
            }
    }

    fun clearRandom() = setState {
        copy(random = Uninitialized)
    }

    private fun hideMenu() = setState { copy(menuExpanded = false) }

    private fun showMenu() = setState { copy(menuExpanded = true) }

    private fun checkSavedPartSelection() = withState {
        storage.getSavedSelectedPart().subscribe(
            {
                val selection = if (it.isNullOrEmpty()) {
                    "C"
                } else {
                    it
                }

                setState {
                    copy(parts = PartSelectorItem.getDefaultPartPickerItems(selection))
                }
            },
            {
                Timber.w("No part selection found, going with default.")
                setState {
                    copy(parts = PartSelectorItem.getDefaultPartPickerItems("C"))
                }
            }
        ).disposeOnClear()
    }

    private fun checkLastUpdateTime() = repository.getLastUpdateTime()
        .map { it.timeMs }
        .execute { newTime ->
            copy(updateTime = newTime)
        }

    private fun checkForUpdate() = repository.checkForUpdate()
        .toObservable()
        .execute {
            copy(digest = it)
        }

    private fun stopTimer() {
        timer?.dispose()
    }

    private fun setSelection(selection: String, oldList: List<PartSelectorItem>?) =
        oldList?.map { item ->
            item.copy(selected = selection == item.apiId)
        }

    private fun subscribeToJamDatabase(jamId: Long) {
        val databaseRefresh = repository.observeJamState(jamId)
            .subscribe(
                {
                    setState { copy(activeJamSheetId = it.id) }
                },
                {
                    val message = "Error observing Jam: ${it.message}"
                    Timber.e(message)
                    cancelJam(message)
                }
            )
            .disposeOnClear()

        jamDisposables.add(databaseRefresh)
    }

    private fun subscribeToJamNetwork(it: Jam) {
        val networkRefresh = repository.refreshJamStateContinuously(it.name)
            .subscribe({},
                {
                    val message = "Error refreshing Jam: ${it.message}"
                    Timber.e(message)
                    cancelJam(message)
                }
            )
            .disposeOnClear()

        jamDisposables.add(networkRefresh)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: HudState): HudViewModel
    }

    interface HudViewModelFactoryProvider {
        var hudViewModelFactory: Factory
    }

    companion object : MvRxViewModelFactory<HudViewModel, HudState> {
        const val TIMEOUT_HUD_VISIBLE = 3000L

        override fun create(viewModelContext: ViewModelContext, state: HudState): HudViewModel? {
            val activity =
                (viewModelContext as ActivityViewModelContext).activity<FragmentActivity>()
            val provider = activity as HudViewModelFactoryProvider
            return provider.hudViewModelFactory.create(state)
        }
    }
}
