package com.vgleadsheets.remaster.offline.updates

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.CollapsibleDetailsListModel
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.updates.OfflineUpdateResult
import net.sigmabeta.sage.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.persistentListOf

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
