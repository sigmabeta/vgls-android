package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.Success
import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.mvrx.MvRxViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class HudViewModel(initialState: HudState) : MvRxViewModel<HudState>(initialState) {
    private var timer: Disposable? = null

    init {
        val parts = generatePartsPickerOptions()
        setState { copy(parts = parts) }
    }

    fun onPartSelect(apiId: String) {
        withState { state ->
            setState {
                copy(parts = setSelection(apiId, state.parts))
            }
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

    private fun generatePartsPickerOptions(): List<PartSelectorItem>? {
        val partsEnums = arrayListOf(
            PartSelectorOption.C,
            PartSelectorOption.B,
            PartSelectorOption.E,
            PartSelectorOption.F,
            PartSelectorOption.BASS,
            PartSelectorOption.ALTO,
            PartSelectorOption.VOCAL
        )

        val items = partsEnums.map { PartSelectorItem.fromEnum(it) }
        return setSelection("C", items)
    }

    private fun setSelection(selection: String, oldList: List<PartSelectorItem>?) =
        oldList?.map { item ->
            item.copy(selected = selection == item.apiId)
        }

    companion object {
        const val TIMEOUT_HUD_VISIBLE = 3000L
    }
}
