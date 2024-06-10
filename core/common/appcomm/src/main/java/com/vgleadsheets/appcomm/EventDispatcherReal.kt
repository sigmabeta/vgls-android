package com.vgleadsheets.appcomm

class EventDispatcherReal(
    private val navFunction: (String) -> Unit,
    private val navBackFunction: () -> Unit,
    private val showSnackbarFunction: (VglsEvent.ShowSnackbar) -> Unit,
    private val showBottomBar: () -> Unit,
    private val hideBottomBar: () -> Unit,
) : EventDispatcher {
    private val eventSinks = mutableSetOf<EventSink>()

    override val sendEvent: (VglsEvent) -> Unit = { event ->
        handleBottomBar(event)
        when (event) {
            is VglsEvent.NavigateTo -> navigateTo(event.destination)
            is VglsEvent.NavigateBack -> navigateBack()
            is VglsEvent.ShowSnackbar -> launchSnackbar(event)
            else -> sendToSinks(event)
        }
    }

    override fun addEventSink(sink: EventSink) {
        eventSinks.add(sink)
    }

    override fun removeEventSink(sink: EventSink) {
        eventSinks.remove(sink)
    }

    private fun launchSnackbar(event: VglsEvent.ShowSnackbar) {
        showSnackbarFunction(event)
    }

    private fun navigateBack() {
        navBackFunction()
    }

    @Suppress("SwallowedException")
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

    private fun sendToSinks(event: VglsEvent) {
        eventSinks.forEach {
            it.sendEvent(event)
        }
    }

    private fun handleBottomBar(event: VglsEvent) {
        when (event) {
            is VglsEvent.ShowUiChrome -> showBottomBar()
            is VglsEvent.HideUiChrome -> hideBottomBar()
        }
    }
}
