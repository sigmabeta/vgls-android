package com.vgleadsheets.composables.previews

import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction

internal class PreviewActionSink(private val actionHandler: (VglsAction) -> Unit): ActionSink {
    override fun sendAction(action: VglsAction) {
        actionHandler(action)
    }
}
