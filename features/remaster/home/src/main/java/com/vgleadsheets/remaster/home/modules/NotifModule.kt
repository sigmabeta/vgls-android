package com.vgleadsheets.remaster.home.modules

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.NotifListModel
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.notif.NotifCategory
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.remaster.home.HomeModule
import com.vgleadsheets.remaster.home.HomeModuleState
import com.vgleadsheets.remaster.home.Priority
import com.vgleadsheets.ui.StringProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class NotifModule @Inject constructor(
    private val notifManager: NotifManager,
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
                    moduleName = this.javaClass.simpleName,
                    shouldShow = notifs.isNotEmpty(),
                    title = title(),
                    items = notifs.map { notif ->
                        NotifListModel(
                            dataId = notif.id,
                            title = stringProvider.getString(notif.title),
                            description = notif.description,
                            actionLabel = notif.actionLabel,
                            action = notif.action,
                            isError = notif.category == NotifCategory.ERROR
                        )
                    },
                )
            )
        }
        .withLoadingState()
        .withErrorState()
}
