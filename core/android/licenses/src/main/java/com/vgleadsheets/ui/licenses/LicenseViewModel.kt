package com.vgleadsheets.ui.licenses

import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LicenseViewModel @Inject constructor(
    override val hatchet: Hatchet,
    override val dispatchers: VglsDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    private val stringProvider: StringProvider,
) : VglsViewModel<State>() {
    override fun initialState() = State("file:///android_asset/open_source_licenses.html")

    override fun handleAction(action: VglsAction) {
        hatchet.d("${this.javaClass.simpleName} - Handling action: $action")
        when (action) {
            is VglsAction.Resume -> resume()
        }
    }

    override fun handleEvent(event: VglsEvent) {
        hatchet.d("${this.javaClass.simpleName} - Handling event: $event")
    }

    private fun resume() {
        updateTitle()
    }

    private fun updateTitle() {
        val state = internalUiState.value
        val titleModel = state.title(stringProvider)

        if (titleModel.title != null) {
            emitEvent(
                VglsEvent.UpdateTitle(
                    title = titleModel.title,
                    shouldShowBack = titleModel.shouldShowBack,
                    source = "License",
                )
            )
        }
    }
}
