package com.vgleadsheets.list

open class ListEvent {
    data class NavigateTo(val destination: String): ListEvent()
}
