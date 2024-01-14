package com.vgleadsheets.remaster.home

import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class ViewModelImpl @AssistedInject constructor(
    @Assisted navigateTo: (String) -> Unit,
) : VglsViewModel<State, Event>(
    initialState = State()
)
