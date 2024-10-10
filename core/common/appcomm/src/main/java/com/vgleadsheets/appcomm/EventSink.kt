package com.vgleadsheets.appcomm

interface EventSink {
    fun sendEvent(event: VglsEvent)
}
