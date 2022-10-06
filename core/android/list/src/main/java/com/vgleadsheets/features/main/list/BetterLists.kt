package com.vgleadsheets.features.main.list

import android.content.res.Resources
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.list.sections.*

object BetterLists {
    suspend fun generateList(
        config: BetterListConfig,
        resources: Resources,
    ): List<ListModel> {
        return listOf(
            Actions.listItems(
                config.actionsConfig.shouldShow,
                config.actionsConfig.actionList
            ),
            Content.listItems(
                config.contentConfig.isDataReady,
                config.contentConfig.dataGenerator
            ),
            EmptyState.listItems(
                config.emptyConfig.shouldShow,
                config.emptyConfig.emptyStateIconId,
                config.emptyConfig.emptyStateMissingThingName,
                config.emptyConfig.emptyStateGenerator,
                resources
            ),
            ErrorState.listItems(
                config.errorConfig.shouldShow,
                config.errorConfig.shouldShowDeveloperMessage,
                config.errorConfig.failedOperationName,
                config.errorConfig.messageForDeveloper,
                resources,
                config.errorConfig.messageForUser,
                config.errorConfig.errorStateGenerator
            ),
            LoadingState.listItems(
                config.loadingConfig.isDataLoading,
                config.loadingConfig.loadingItemStyle,
                config.loadingConfig.loadingGenerator,
            )
        ).flatten()
    }
}
