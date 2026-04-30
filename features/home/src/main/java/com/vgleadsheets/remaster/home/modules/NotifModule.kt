package com.vgleadsheets.remaster.home.modules

import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.di.ActionDeserializer
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.NotifListModel
import net.sigmabeta.sage.list.DelayManager
import com.vgleadsheets.notif.NotifCategory
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import net.sigmabeta.sage.ui.StringProvider
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotifModule @Inject constructor(
    private val notifManager: NotifManager,
    private val actionDeserializer: ActionDeserializer,
    private val stringProvider: StringProvider,
    delayManager: DelayManager,
) : HomeModule(
    priority = Priority.HIGHEST,
    delayManager,
) {
    override fun loadingType() = LoadingType.NOTIF

    override fun title() = null

    override fun state() = notifManager
        .notifState
        .map { it.notifs.values.toList() }
        .map { notifs ->
            LCE.Content(
                HomeModuleState(
                    moduleName = "NotifModule",
                    shouldShow = notifs.isNotEmpty(),
                    title = title(),
                    items = notifs.map { notif ->
                        val title = stringProvider.getString(notif.title)
                        NotifListModel(
                            dataId = notif.id,
                            title = title,
                            description = notif.description,
                            actionLabel = notif.actionLabel,
                            action = actionDeserializer.recreateAction(notif.action),
                            isError = notif.category == NotifCategory.ERROR
                        )
                    },
                )
            )
        }
        .withLoadingState()
        .withErrorState()
}
