package com.vgleadsheets.appcomm

interface EventDispatcher {
    val sendEvent: (VglsEvent) -> Unit
    fun addEventSink(sink: EventSink)
    fun removeEventSink(sink: EventSink)
}
