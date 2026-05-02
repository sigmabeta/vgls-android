package com.vgleadsheets.ui.licenses

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreen
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider
import javax.inject.Inject

@HiltViewModel
class LicenseViewModel @Inject constructor(
    override val hatchet: Hatchet,
    override val dispatchers: SageDispatchers,
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

    override fun sendInitAction() = sendAction(SageAction.InitNoArgs)

    override fun handleAction(action: SageAction) {
        hatchet.d("${this.javaClass.simpleName} - Handling action: $action")
        when (action) {
            is SageAction.Resume -> resume()
        }
    }

    override fun handleEvent(event: SageEvent) {
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
                SageEvent.UpdateTitle(
                    title = titleModel.title,
                    shouldShowBack = titleModel.shouldShowBack,
                    source = "License",
                )
            )
        }
    }
}
