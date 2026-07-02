package com.vgleadsheets.remaster.updates

import com.vgleadsheets.model.updates.AppUpdate
import com.vgleadsheets.strings.VglsStringId
import kotlinx.collections.immutable.toImmutableList
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.CollapsibleDetailsListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.ui.StringProvider

data class State(
    val updates: LCE<List<AppUpdate>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(VglsStringId.SCREEN_TITLE_UPDATES),
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
