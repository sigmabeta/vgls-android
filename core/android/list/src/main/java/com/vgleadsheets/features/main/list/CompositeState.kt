package com.vgleadsheets.features.main.list

import com.vgleadsheets.mvrx.VglsState

interface CompositeState<CompositeContentType : ListContent> : VglsState {
    val contentLoad: CompositeContentType

    fun content() = contentLoad

    override fun failure() = contentLoad.failure()

    override fun isLoading() = contentLoad.isLoading()

    override fun hasFailed() = contentLoad.hasFailed()

    override fun isEmpty() = contentLoad.isEmpty()

    override fun isReady() = contentLoad.isReady()

    override fun isFullyLoaded() = contentLoad.isFullyLoaded()
}
