package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class State(
    val moduleStatesByPriority: Map<HomeModule, LCE<HomeModuleState>> = emptyMap()
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.APP_NAME),
        shouldShowBack = false,
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        if (moduleStatesByPriority.isEmpty()) {
            return persistentListOf()
        }

        val sortedModuleStatesByPriority = moduleStatesByPriority
            .entries
            .sortedBy { it.key.priority }

        val highestStillLoadingPriority = sortedModuleStatesByPriority
            .lastOrNull { it.value !is LCE.Content && it.value !is LCE.Error }
            ?.key
            ?.priority

        val statesToShow = sortedModuleStatesByPriority
            // Counter-intuitive verbally, but makes sense numerical
            .filter { it.key.priority.ordinal <= (highestStillLoadingPriority?.ordinal ?: Int.MAX_VALUE) }
            .map { it.value }

        return statesToShow
            .map { it.toListItems() }
            .flatten()
            .toImmutableList()
    }

    private fun LCE<HomeModuleState>.toListItems() = when (this) {
        is LCE.Loading -> loadingListModels()
        is LCE.Content -> this.data.toListItems()
        is LCE.Error -> errorListModels()
        LCE.Uninitialized -> emptyList()
    }

    private fun LCE.Loading.loadingListModels() = List(2) { index ->
        LoadingListModel(
            withImage = true,
            withCaption = true,
            loadOperationName = this.operationName,
            loadPositionOffset = index,
        )
    }

    private fun LCE.Error.errorListModels() = listOf(
        ErrorStateListModel(
            failedOperationName = this.operationName,
            errorString = "Failed to load data for home screen module: ${error.message}",
        )
    )
}
