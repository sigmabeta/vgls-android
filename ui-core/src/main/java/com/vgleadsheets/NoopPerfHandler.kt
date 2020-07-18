package com.vgleadsheets

class NoopPerfHandler: PerfHandler {
    override fun onTitleLoaded()  = Unit
    override fun onTransitionStart()  = Unit
    override fun onPartialContentLoad()  = Unit
    override fun onFullContentLoad()  = Unit
    override fun onLoadFail()  = Unit
}