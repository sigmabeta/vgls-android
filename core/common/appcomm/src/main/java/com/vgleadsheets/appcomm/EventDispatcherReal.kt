package com.vgleadsheets.appcomm

class EventDispatcherReal : EventDispatcher {
    private val eventSinks = mutableSetOf<EventSink>()

    override val sendEvent: (VglsEvent) -> Unit = { event ->
        sendToSinks(event)
    }

    override fun addEventSink(sink: EventSink) {
        eventSinks.add(sink)
    }

    override fun removeEventSink(sink: EventSink) {
        eventSinks.remove(sink)
    }

    private fun sendToSinks(event: VglsEvent) {
        eventSinks.forEach {
            it.sendEvent(event)
        }
    }
}
