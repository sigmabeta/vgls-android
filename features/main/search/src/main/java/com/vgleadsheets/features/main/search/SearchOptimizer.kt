package com.vgleadsheets.features.main.search

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success

class SearchOptimizer<FieldType> {
    fun skipLoadingIfPreviouslyLoaded(oldValue: Async<List<FieldType>>, newValue: Async<List<FieldType>>): Async<List<FieldType>> {
        if (oldValue is Success && oldValue().isNotEmpty() && newValue is Loading) {
            return oldValue
        }

        return newValue
    }
}