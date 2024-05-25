package com.vgleadsheets.appcomm


class EventDispatcher(
    private val navigateTo: (String)-> Unit,
    private val navigateBack: () -> Unit,
) {
    private val eventSinks = mutableSetOf<EventSink>()

    val sendEvent: (VglsEvent) -> Unit = { event ->
//        hatchet.d("Sending event: $it")
        when (event) {
            is VglsEvent.NavigateTo -> navigateTo(event.destination)
            is VglsEvent.NavigateBack -> navigateBack()
            else -> sendToSinks(event)
        }
    }

    fun addEventSink(sink: EventSink) {
        eventSinks.add(sink)
    }

    fun removeEventSink(sink: EventSink) {
        eventSinks.remove(sink)
    }

    private fun sendToSinks(event: VglsEvent) {
        eventSinks.forEach {
            it.sendEvent(event)
        }
    }
}
