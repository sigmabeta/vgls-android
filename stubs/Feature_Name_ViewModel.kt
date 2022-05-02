package com.vgleadsheets.features.main.feature_name_

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class Feature_Name_ViewModel @AssistedInject constructor(
    @Assisted initialState: Feature_Name_State,
    private val repository: Repository
) : MvRxViewModel<Feature_Name_State>(initialState) {

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: Feature_Name_State): Feature_Name_ViewModel
    }

    companion object : MvRxViewModelFactory<Feature_Name_ViewModel, Feature_Name_State> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: Feature_Name_State
        ): Feature_Name_ViewModel? {
            val fragment: Feature_Name_Fragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.feature_nameViewModelFactory.create(state)
        }
    }
}
