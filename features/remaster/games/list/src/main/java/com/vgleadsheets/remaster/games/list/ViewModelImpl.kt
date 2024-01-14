package com.vgleadsheets.remaster.games.list

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class ViewModelImpl @AssistedInject constructor(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    @Assisted navigateTo: (String) -> Unit,
) : VglsViewModel<State, Event>(
    initialState = State()
) {

}
