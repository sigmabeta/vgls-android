package com.vgleadsheets.appcomm


open class VglsEvent {
    data object NavigateBack : VglsEvent()
    data class NavigateTo(val destination: String) : VglsEvent()

    data class UpdateTitle(
        val title: String? = null,
        val subtitle: String? = null,
        val imageUrl: String? = null,
        val shouldShowBack: Boolean = true,
    ) : VglsEvent()
}
