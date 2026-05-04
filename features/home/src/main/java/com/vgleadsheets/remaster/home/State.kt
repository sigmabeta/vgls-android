package com.vgleadsheets.remaster.home

import com.vgleadsheets.strings.StringId
import kotlinx.collections.immutable.persistentListOf
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.ui.StringProvider

data class State(
    val moduleStatesByPriority: Map<ModuleDetails, LCE<HomeModuleState>> = emptyMap()
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.APP_NAME),
        shouldShowBack = false,
    )

    override fun toListItems(stringProvider: StringProvider): List<ListModel> {
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
    }

    @Suppress("MagicNumber")
    private fun LCE<HomeModuleState>.toListItems() = withStandardErrorAndLoading(
        loadingType = LoadingType.TEXT_CAPTION_IMAGE,
    ) {
        this.data.toListItems()
    }
}
