package com.vgleadsheets.features.main.list.async

interface ListData {
    fun isEmpty(): Boolean
    fun isLoading(): Boolean
    fun canShowPartialData(): Boolean
}
