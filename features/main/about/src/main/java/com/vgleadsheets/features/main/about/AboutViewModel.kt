package com.vgleadsheets.features.main.about

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class AboutViewModel @AssistedInject constructor(
    @Assisted initialState: AboutState,
    private val repository: Repository
) : MvRxViewModel<AboutState>(initialState) {

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: AboutState): AboutViewModel
    }

    companion object : MvRxViewModelFactory<AboutViewModel, AboutState> {
        override fun create(viewModelContext: ViewModelContext, state: AboutState): AboutViewModel? {
            val fragment: AboutFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.aboutViewModelFactory.create(state)
        }
    }
}
