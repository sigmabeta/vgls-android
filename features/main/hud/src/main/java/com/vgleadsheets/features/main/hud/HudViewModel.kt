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
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Suppress("TooManyFunctions")
class HudViewModel @AssistedInject constructor(
    @Assisted initialState: HudState,
    private val repository: Repository
) : MvRxViewModel<HudState>(initialState) {
    private var timer: Disposable? = null

    init {
        resetAvailableParts()
        checkLastUpdateTime()
        checkForUpdate()
    }

    fun onMenuClick() {
        setState {
            return@setState if (menuExpanded) {
                copy(menuExpanded = false)
            } else {
                copy(menuExpanded = true)
            }
        }
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

    fun onRandomSelectClick() {
        repository
            .getAllSongs()
            .firstOrError()
            .map { it.random() }
            .execute {
                copy(random = it)
            }
    }

    fun clearRandom() = setState {
        copy(random = Uninitialized)
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

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: HudState): HudViewModel
    }

    interface HudContainer {
        fun getHudFragment(): HudFragment
    }

    companion object : MvRxViewModelFactory<HudViewModel, HudState> {
        const val TIMEOUT_HUD_VISIBLE = 3000L

        override fun create(viewModelContext: ViewModelContext, state: HudState): HudViewModel? {
            val activity =
                (viewModelContext as ActivityViewModelContext).activity<FragmentActivity>()
            val getter = activity as HudContainer
            val fragment = getter.getHudFragment()
            return fragment.hudViewModelFactory.create(state)
        }
    }
}
