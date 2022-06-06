package com.vgleadsheets.features.main.list

import com.airbnb.mvrx.MvRxState

interface BetterCompositeState<CompositeContentType : ListContent> : MvRxState {
    val contentLoad: CompositeContentType

    fun content() = contentLoad

    fun failure() = contentLoad.failure()

    fun isLoading() = contentLoad.isLoading()

    fun hasFailed() = contentLoad.hasFailed()

    fun isEmpty() = contentLoad.isEmpty()

    fun isReady() = contentLoad.isReady()

    fun isFullyLoaded() = contentLoad.isFullyLoaded()
}
