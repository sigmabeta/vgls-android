package com.vgleadsheets.ui.licenses

import androidx.lifecycle.viewModelScope
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreen
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.VglsAction
import net.sigmabeta.sage.appcomm.VglsEvent
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.coroutines.VglsDispatchers
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LicenseViewModel @Inject constructor(
    override val hatchet: Hatchet,
    override val dispatchers: VglsDispatchers,
    override val analytics: Analytics,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    private val stringProvider: StringProvider,
    override val showDebugProvider: ShowDebugProvider,
) : VglsViewModel<State>() {
    override val screenIdentifier = AnalyticsScreen.LICENSE

    init {
        viewModelScope.launch(dispatchers.main) {
            sendInitAction()
        }
    }

    override fun initialState() = State("file:///android_asset/open_source_licenses.html")

    override fun sendInitAction() = sendAction(VglsAction.InitNoArgs)

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
