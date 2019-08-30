package com.vgleadsheets.mainstate

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.mvrx.MvRxViewModel

class MainActivityViewModel constructor(
    initialState: MainActivityState
) : MvRxViewModel<MainActivityState>(initialState) {
    fun onSearchLaunch() {
        setState { copy(searchClicked = false, searchVisible = true) }
    }

    fun subscribeToSearchClicks(fragmentRouter: FragmentRouter) = fragmentRouter.searchClicks()
        .subscribe {
            setState { copy(searchClicked = true) }
        }

    fun subscribeToSearchTextEntry(fragmentRouter: FragmentRouter) = fragmentRouter.searchEvents()
        .subscribe {
            setState { copy(searchQuery = it) }
        }

    fun onBackPressed() = withState { state ->
        if (state.searchVisible) {
            setState { copy(hideSearch = true) }
        } else {
            setState { copy(popBackStack = true) }
        }
    }

    fun onSearchHide() {
        setState { copy(hideSearch = false, searchVisible = false) }
    }

    fun onBackStackPop() {
        setState { copy(popBackStack = false) }
    }
}
