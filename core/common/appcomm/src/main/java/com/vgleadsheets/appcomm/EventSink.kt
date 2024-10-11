package com.vgleadsheets.appcomm

fun interface EventSink {
    fun sendEvent(event: VglsEvent)
}
