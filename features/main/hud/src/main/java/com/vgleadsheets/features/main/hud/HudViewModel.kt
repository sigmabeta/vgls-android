package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.Success
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.mvrx.MvRxViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class HudViewModel(initialState: HudState) : MvRxViewModel<HudState>(initialState) {
    private var timer: Disposable? = null

    init {
        resetAvailableParts()
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
            copy(parts = setSelection(apiId, state.parts))
        }
    }

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

    fun onSearchExit() = withState { state ->
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
        timer?.dispose()
        if (!state.hudVisible) {
            setState { copy(hudVisible = true) }
        }
    }

    fun startHudTimer() {
        timer = Observable.timer(TIMEOUT_HUD_VISIBLE, TimeUnit.MILLISECONDS)
            .execute { timer ->
                if (timer is Success) {
                    copy(hudVisible = false)
                } else {
                    this
                }
            }
    }

    private fun setSelection(selection: String, oldList: List<PartSelectorItem>?) =
        oldList?.map { item ->
            item.copy(selected = selection == item.apiId)
        }

    companion object {
        const val TIMEOUT_HUD_VISIBLE = 3000L
    }
}
