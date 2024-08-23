package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class State(
    val moduleStatesByPriority: Map<ModuleDetails, LCE<HomeModuleState>> = emptyMap()
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
            .firstOrNull {
                val stateLCE = it.value
                val isLoading = stateLCE is LCE.Content && stateLCE.data.isLoading
                isLoading || stateLCE == LCE.Uninitialized
            }
            ?.key
            ?.priority

        val prioritiesToShow = sortedModuleStatesByPriority
            // Counter-intuitive verbally, but makes sense numerical
            .filter { it.key.priority.ordinal - 1 <= (highestStillLoadingPriority?.ordinal ?: Int.MAX_VALUE) }

        // For debugging
        // println("Showing states. Highest still loading prio $highestStillLoadingPriority")
        // prioritiesToShow.forEach { entry ->
        //     println("${entry.key.priority} || ${entry.value.javaClass.simpleName} || ${entry.key.name} ")
        // }

        val statesToShow = prioritiesToShow
            .map { it.value }

        return statesToShow
            .map { it.toListItems() }
            .flatten()
            .toImmutableList()
    }

    @Suppress("MagicNumber")
    private fun LCE<HomeModuleState>.toListItems() = when (this) {
        is LCE.Loading -> loading(operationName, LoadingType.TEXT_CAPTION_IMAGE, 20)
        is LCE.Content -> this.data.toListItems()
        is LCE.Error -> error(operationName, error)
        LCE.Uninitialized -> emptyList()
    }
}
