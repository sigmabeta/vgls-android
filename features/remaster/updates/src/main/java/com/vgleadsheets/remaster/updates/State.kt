package com.vgleadsheets.remaster.updates

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.CollapsibleDetailsListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.updates.AppUpdate
import com.vgleadsheets.time.PublishDateUtils.toLongDateText
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.toImmutableList

data class State(
    val updates: LCE<List<AppUpdate>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_SETTINGS),
        shouldShowBack = true
    )

    override fun toListItems(stringProvider: StringProvider) = updates.withStandardErrorAndLoading(
        loadingType = LoadingType.SINGLE_TEXT,
        loadingItemCount = 6,
    ) {
        data.mapIndexed { index, update ->
            val releaseDate = update.releaseDate.toLongDateText()

            CollapsibleDetailsListModel(
                dataId = update.versionCode.toLong(),
                title = "${update.versionName}: $releaseDate",
                detailItems = update.changes.toImmutableList(),
                initiallyCollapsed = index != 0
            )
        }
    }
}
