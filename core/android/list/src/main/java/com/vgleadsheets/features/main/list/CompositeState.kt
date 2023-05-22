package com.vgleadsheets.features.main.list

import com.airbnb.mvrx.MavericksState

interface CompositeState<CompositeContentType : ListContent> : MavericksState {
    val contentLoad: CompositeContentType

    fun content() = contentLoad

    fun failure() = contentLoad.failure()

    fun isLoading() = contentLoad.isLoading()

    fun hasFailed() = contentLoad.hasFailed()

    fun isEmpty() = contentLoad.isEmpty()

    fun isReady() = contentLoad.isReady()

    fun isFullyLoaded() = contentLoad.isFullyLoaded()
}
