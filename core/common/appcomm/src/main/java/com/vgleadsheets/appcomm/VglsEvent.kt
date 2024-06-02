package com.vgleadsheets.appcomm

open class VglsEvent {
    open val source: String = "Unknown"

    data class NavigateBack(override val source: String) : VglsEvent()
    data class NavigateTo(val destination: String, override val source: String) : VglsEvent()

    data class ShowSnackbar(
        val message: String,
        val withDismissAction: Boolean,
        val actionDetails: SnackbarActionDetails? = null,
        override val source: String,
    ) : VglsEvent() {
        data class SnackbarActionDetails(
            val actionSink: ActionSink,
            val clickAction: VglsAction,
            val clickActionLabel: String,
        )
    }

    data class UpdateTitle(
        val title: String? = null,
        val subtitle: String? = null,
        val shouldShowBack: Boolean = true,
        override val source: String,
    ) : VglsEvent()
}
