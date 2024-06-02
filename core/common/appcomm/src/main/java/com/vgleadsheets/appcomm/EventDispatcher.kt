package com.vgleadsheets.appcomm

class EventDispatcher(
    private val navFunction: (String) -> Unit,
    private val navBackFunction: () -> Unit,
    private val showSnackbarFunction: (VglsEvent.ShowSnackbar) -> Unit,
) {
    private val eventSinks = mutableSetOf<EventSink>()

    val sendEvent: (VglsEvent) -> Unit = { event ->
        when (event) {
            is VglsEvent.NavigateTo -> navigateTo(event.destination)
            is VglsEvent.NavigateBack -> navigateBack()
            is VglsEvent.ShowSnackbar -> launchSnackbar(event)
            else -> sendToSinks(event)
        }
    }

    private fun launchSnackbar(event: VglsEvent.ShowSnackbar) {
        showSnackbarFunction(event)
    }

    private fun navigateBack() {
        navBackFunction()
    }

    private fun navigateTo(destination: String) {
        try {
            navFunction(destination)
        } catch (ex: IllegalArgumentException) {
            sendEvent(
                VglsEvent.ShowSnackbar(
                    message = "Unimplemented screen: $destination",
                    withDismissAction = true,
                    source = "Navigation"
                )
            )
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
