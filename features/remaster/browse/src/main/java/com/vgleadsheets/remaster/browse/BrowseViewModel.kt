package com.vgleadsheets.remaster.browse

import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor() : VglsViewModel<BrowseState, BrowseEvent>(
    initialState = BrowseState()
)
