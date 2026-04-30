package com.vgleadsheets.composables.previews

import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.VglsAction

class PreviewActionSink(private val actionHandler: (VglsAction) -> Unit = {}) : ActionSink {
    override fun sendAction(action: VglsAction) {
        actionHandler(action)
    }
}
