package com.vgleadsheets.mainstate

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.mvrx.MvRxViewModel

class MainActivityViewModel constructor(
    initialState: MainActivityState
) : MvRxViewModel<MainActivityState>(initialState) {
    fun subscribeToSearchTextEntry(fragmentRouter: FragmentRouter) = fragmentRouter.searchEvents()
        .subscribe {
            setState { copy(searchQuery = it) }
        }
}
