package com.vgleadsheets.remaster.updates

import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.CollapsibleDetailsListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ListState
import com.vgleadsheets.model.updates.AppUpdate
import net.sigmabeta.sage.ui.StringId
import net.sigmabeta.sage.ui.StringProvider
import kotlinx.collections.immutable.toImmutableList

data class State(
    val updates: LCE<List<AppUpdate>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_UPDATES),
        shouldShowBack = true
    )

    override fun toListItems(stringProvider: StringProvider) = updates.withStandardErrorAndLoading(
        loadingType = LoadingType.SINGLE_TEXT,
        loadingItemCount = 6,
    ) {
        data.mapIndexed { index, update ->
            val releaseDate = update.releaseDate

            CollapsibleDetailsListModel(
                dataId = update.versionCode.toLong(),
                title = "${update.versionName} - $releaseDate",
                detailItems = update.changes.toImmutableList(),
                initiallyCollapsed = index != 0
            )
        }
    }
}
