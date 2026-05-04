package com.vgleadsheets.remaster.offline.updates

import com.vgleadsheets.model.updates.OfflineUpdateResult
import com.vgleadsheets.strings.StringId
import kotlinx.collections.immutable.persistentListOf
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.CollapsibleDetailsListModel
import net.sigmabeta.sage.components.EmptyStateListModel
import net.sigmabeta.sage.components.ErrorStateListModel
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringProvider

data class State(
    val results: LCE<List<OfflineUpdateResult>> = LCE.Uninitialized,
    val formattedDateTimes: Map<Int, String> = emptyMap(),
    val formattedServerTimes: Map<Int, String> = emptyMap(),
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_OFFLINE_UPDATES),
        shouldShowBack = true,
    )

    override fun toListItems(stringProvider: StringProvider): List<ListModel> {
        val error = results
        if (error is LCE.Error) {
            return listOf(
                ErrorStateListModel(
                    failedOperationName = error.operationName,
                    errorString = stringProvider.getString(StringId.OFFLINE_UPDATES_ERROR),
                    error = error.error,
                )
            )
        }

        return results.withStandardErrorAndLoading(
            loadingType = LoadingType.SINGLE_TEXT,
            loadingItemCount = 6,
        ) {
            if (data.isEmpty()) {
                return@withStandardErrorAndLoading listOf(
                    EmptyStateListModel(
                        icon = Icon.OFFLINE_OUTLINE,
                        explanation = stringProvider.getString(StringId.OFFLINE_UPDATES_EMPTY),
                        showCrossOut = false,
                    )
                )
            }

            data.mapIndexed { index, result ->
                CollapsibleDetailsListModel(
                    dataId = result.id.toLong(),
                    title = formattedDateTimes[result.id].orEmpty(),
                    detailItems = persistentListOf(
                        stringProvider.getStringOneArg(StringId.OFFLINE_UPDATE_FIELD_STATUS, result.status.name),
                        stringProvider.getStringOneArg(
                            StringId.OFFLINE_UPDATE_FIELD_SERVER_TIME,
                            formattedServerTimes[result.id].orEmpty(),
                        ),
                        stringProvider.getStringOneInt(
                            StringId.OFFLINE_UPDATE_FIELD_UPDATED_SONGS,
                            result.updatedSongs,
                        ),
                        stringProvider.getStringOneInt(
                            StringId.OFFLINE_UPDATE_FIELD_SUCCESSFUL_OFFLINES,
                            result.successfulOfflines,
                        ),
                    ),
                    initiallyCollapsed = index != 0,
                )
            }
        }
    }
}
