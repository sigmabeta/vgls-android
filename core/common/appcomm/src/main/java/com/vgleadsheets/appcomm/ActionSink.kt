package com.vgleadsheets.appcomm

fun interface ActionSink {
    fun sendAction(action: VglsAction)
}
