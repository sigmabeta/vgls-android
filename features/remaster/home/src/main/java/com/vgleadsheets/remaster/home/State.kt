package com.vgleadsheets.remaster.home

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.NotifListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.notif.Notif
import com.vgleadsheets.notif.NotifCategory
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class State(
    val notifs: List<Notif> = emptyList()
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.APP_NAME),
        shouldShowBack = false,
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return notifs.map {
            NotifListModel(
                it.id,
                it.title,
                it.description,
                it.actionLabel,
                it.action,
                it.category == NotifCategory.ERROR
            )
        }.toImmutableList()
    }
}
