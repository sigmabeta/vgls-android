package com.vgleadsheets.features.main.list

import android.content.res.Resources
import com.airbnb.mvrx.MvRxState
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title

object BetterLists {
    fun <StateType : MvRxState, ClickHandlerType : ListItemClicks> generateList(
        config: BetterListConfig<StateType, ClickHandlerType>,
        resources: Resources,
    ): List<ListModel> {
        return listOf(
            Title.listItems(
                config.titleConfig.title,
                config.titleConfig.subtitle,
                config.titleConfig.onImageLoadSuccess,
                config.titleConfig.onImageLoadFail,
                resources,
                config.titleConfig.onMenuButtonClick,
                config.titleConfig.photoUrl,
                config.titleConfig.placeholder,
                config.titleConfig.shouldShow,
                config.titleConfig.isLoading,
                config.titleConfig.titleGenerator,
            ),
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
