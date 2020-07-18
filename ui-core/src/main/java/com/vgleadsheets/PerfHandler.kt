package com.vgleadsheets

interface PerfHandler {
    fun onTitleLoaded()

    fun onTransitionStart()

    fun onPartialContentLoad()

    fun onFullContentLoad()

    fun onLoadFail()
}