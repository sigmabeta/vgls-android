package com.vgleadsheets.features.main.list.async

interface ListData {
    fun isEmpty(): Boolean

    fun isUninitialized(): Boolean

    fun isLoading(): Boolean

    fun getFailReason(): Throwable?

    fun isSuccess(): Boolean
}
