package com.vgleadsheets.sheets

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class SheetListViewModel @AssistedInject constructor(
    @Assisted initialState: SheetListState,
    private val repository: Repository
) : MvRxViewModel<SheetListState>(initialState) {
    init {
        fetchSheets()
    }

    private fun fetchSheets() = withState { state ->
        repository.getSheets(state.gameId)
            .execute { data ->
                copy(data = data)
            }
    }

    fun onItemClick(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SheetListState): SheetListViewModel
    }

    companion object : MvRxViewModelFactory<SheetListViewModel, SheetListState> {
        override fun create(viewModelContext: ViewModelContext, state: SheetListState): SheetListViewModel? {
            val fragment: SheetListFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.sheetListViewModelFactory.create(state)
        }
    }
}