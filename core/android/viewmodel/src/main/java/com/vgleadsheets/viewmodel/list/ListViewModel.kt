package com.vgleadsheets.viewmodel.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vgleadsheets.list.BrainProvider
import com.vgleadsheets.list.ListEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.state.VglsAction
import com.vgleadsheets.ui.StringProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ListViewModel @AssistedInject constructor(
    private val brainProvider: BrainProvider,
    val stringProvider: StringProvider,
    @Assisted destination: Destination,
    @Assisted eventHandler: (ListEvent) -> Unit,
    @Assisted idArg: Long,
    @Assisted stringArg: String?,
) : ViewModel() {
    private val brain: ListViewModelBrain = brainProvider.provideBrain(
        destination,
        viewModelScope,
    )

    val uiState = brain.uiState
    private val uiEvents = brain.uiEvents

    init {
        val initAction = when {
            (idArg > 0L) -> VglsAction.InitWithId(idArg)
            stringArg != null -> VglsAction.InitWithString(stringArg)
            else -> VglsAction.InitNoArgs
        }

        handleAction(initAction)
        uiEvents
            .onEach { eventHandler(it) }
            .launchIn(viewModelScope)
    }

    fun handleAction(action: VglsAction) = brain.handleAction(action)
}
