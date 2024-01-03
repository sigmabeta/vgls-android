package com.vgleadsheets.remaster.home

import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: VglsRepository,
) : VglsViewModel<HomeState, HomeEvent>(
    initialState = HomeState()
) { }
