package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.components.NotifListModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.notif.NotifCategory
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotifModule @Inject constructor(
    private val notifManager: NotifManager,
    private val stringProvider: StringProvider,
    dispatchers: VglsDispatchers,
    coroutineScope: CoroutineScope,
) : HomeModule(
    dispatchers,
    coroutineScope,
) {
    override fun state() = notifManager
        .notifState
        .map { it.notifs.values.toList() }
        .map { notifs ->
            HomeModuleState(
                shouldShow = notifs.isNotEmpty(),
                priority = Priority.HIGH,
                title = null,
                items = notifs.map { notif ->
                    NotifListModel(
                        dataId = notif.id,
                        title = stringProvider.getString(notif.title),
                        description = notif.description,
                        actionLabel = notif.actionLabel,
                        action = notif.action,
                        isError = notif.category == NotifCategory.ERROR
                    )
                }
            )
        }
}
