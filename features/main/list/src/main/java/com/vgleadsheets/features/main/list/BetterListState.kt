package com.vgleadsheets.features.main.list

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState

interface BetterListState<DataType> : MvRxState {
    val dataLoad: Async<List<DataType>>

    fun data() = dataLoad.data()

    fun failure() = dataLoad.failure()

    fun isLoading() = dataLoad is Loading

    fun hasFailed() = dataLoad is Fail

    fun isEmpty() = data()?.isEmpty() ?: false

    fun isReady() = !data().isNullOrEmpty()

    open fun isFullyLoaded() = isReady()
}
