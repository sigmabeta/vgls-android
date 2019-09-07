package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class HudViewModel @AssistedInject constructor(
    @Assisted initialState: HudState,
    private val repository: Repository
) : MvRxViewModel<HudState>(initialState) {

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: HudState): HudViewModel
    }

    companion object : MvRxViewModelFactory<HudViewModel, HudState> {
        override fun create(viewModelContext: ViewModelContext, state: HudState): HudViewModel? {
            val fragment: HudFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.hudViewModelFactory.create(state)
        }
    }
}
