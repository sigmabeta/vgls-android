package com.vgleadsheets.remaster.home

import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : VglsViewModel<HomeState, HomeEvent>(
    initialState = HomeState()
)
