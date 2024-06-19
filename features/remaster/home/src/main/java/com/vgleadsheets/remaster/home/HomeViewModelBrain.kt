package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeViewModelBrain(
    stringProvider: StringProvider,
    hatchet: Hatchet,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
    private val notifManager: NotifManager,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    dispatchers,
    coroutineScope
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> setup()
            is VglsAction.Resume -> return
            is VglsAction.NotifClearClicked -> removeNotif(action.id)
            is VglsAction.SeeWhatsNewClicked -> onSeeWhatsNewClicked()
            else -> throw IllegalArgumentException("Invalid action for this screen.")
        }
    }

    private fun onSeeWhatsNewClicked() {
        emitEvent(
            VglsEvent.ShowSnackbar(
                message = "What's new screen is unimplemented.",
                withDismissAction = false,
                actionDetails = null,
                source = "Home"
            )
        )
    }

    private fun removeNotif(id: Long) {
        notifManager.removeNotif(id)
    }

    private fun setup() {
        notifManager
            .notifState
            .onEach { notifState ->
                updateState {
                    (it as State).copy(
                        notifs = notifState.notifs.values.toList()
                    )
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }
}
