package com.vgleadsheets.remaster.browse

import androidx.lifecycle.ViewModel
import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.viewmodel.list.VglsListViewModel
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
class BrowseViewModel @Inject constructor(
    override val stringProvider: StringProvider,
    override val analytics: Analytics,
    override val hatchet: Hatchet,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
    private val tagRepository: TagRepository,
) : VglsListViewModel<State>() {
    override val screenIdentifier = VglsAnalyticsScreen.BROWSE

    override fun initialState() = State()

    init {
        sendAction(SageAction.InitNoArgs)
    }

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> startLoading()
            is SageAction.Resume -> return
            is Action.DestinationClicked -> onDestinationClicked(action.destination)
        }
    }

    private fun startLoading() {
        collectDatePublishedId()
    }

    private fun collectDatePublishedId() {
        tagRepository.getIdOfPublishDateTagKey()
            .onEach(::onPublishDateIdLoaded)
            .runInBackground()
    }

    private fun onPublishDateIdLoaded(id: Long?) {
        updatePublishDateId(LCE.Content(id))
    }

    private fun updatePublishDateId(id: LCE<Long?>) {
        updateState {
            it.copy(
                publishDateId = id
            )
        }
    }

    private fun onDestinationClicked(destination: String) {
        emitEvent(
            SageEvent.NavigateTo(
                destination,
                Destination.BROWSE.name
            )
        )
    }
}
