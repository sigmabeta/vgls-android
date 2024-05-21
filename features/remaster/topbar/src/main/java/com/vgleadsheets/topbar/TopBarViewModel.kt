package com.vgleadsheets.topbar

import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.state.VglsAction
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
class TopBarViewModel @Inject constructor(
    private val selectedPartManager: SelectedPartManager,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
    private val hatchet: Hatchet,
) : VglsViewModel<TopBarState, TopBarEvent>() {
    override fun initialState() = TopBarState()

    init {
        loadSelectedPart()
    }

    fun updateTitle(title: TitleBarModel) {
        hatchet.v("Updating title: $title")
        internalUiState.update {
            it.copy(model = title)
        }
    }

    fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.Menu -> {}
            is VglsAction.AppBack -> {}
        }
    }

    private fun loadSelectedPart() {
        selectedPartManager
            .selectedPartFlow()
            .onEach { selectedPart ->
                internalUiState.update {
                    it.copy(selectedPart = selectedPart.apiId)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }
}
