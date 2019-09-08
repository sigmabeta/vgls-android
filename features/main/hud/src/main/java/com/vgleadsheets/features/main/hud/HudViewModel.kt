package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.Success
import com.vgleadsheets.mvrx.MvRxViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class HudViewModel(initialState: HudState) : MvRxViewModel<HudState>(initialState) {
    private var timer: Disposable? = null

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

    companion object {
        const val TIMEOUT_HUD_VISIBLE = 3000L
    }
}
