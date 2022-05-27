package com.vgleadsheets.features.main.list

import com.airbnb.mvrx.Async

abstract class SimpleListContent<ContentType>(
    private val contentLoad: Async<List<ContentType>>
) : ListContent {
    fun content() = contentLoad()

    override fun failure() = contentLoad.failure()

    override fun isLoading() = contentLoad.isLoading()

    override fun hasFailed() = contentLoad.hasFailed()

    override fun isEmpty() = contentLoad.content()?.isEmpty() ?: false

    override fun isReady() = contentLoad.isReady()

    override fun isFullyLoaded() = isReady()
}
