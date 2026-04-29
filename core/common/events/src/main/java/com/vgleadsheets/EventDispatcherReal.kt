package com.vgleadsheets

import com.vgleadsheets.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.EventSink
import net.sigmabeta.sage.appcomm.VglsEvent

class EventDispatcherReal(
    private val analytics: Analytics,
) : EventDispatcher {
    private val eventSinks = mutableSetOf<EventSink>()

    override val sendEvent: (VglsEvent) -> Unit = { event ->
        analytics.logVglsEvent(event)
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
