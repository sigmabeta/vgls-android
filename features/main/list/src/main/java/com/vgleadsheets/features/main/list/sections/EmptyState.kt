package com.vgleadsheets.features.main.list.sections

import android.content.res.Resources
import androidx.annotation.DrawableRes
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.list.R

object EmptyState {
    fun listItems(
        shouldShow: Boolean,
        @DrawableRes emptyStateIconId: Int,
        missingThingName: String,
        emptyStateGenerator: (() -> List<ListModel>)? = null,
        // retryAction: (() -> Unit)? = null,
        resources: Resources
    ) = if (!shouldShow) {
        emptyList()
    } else if (emptyStateGenerator != null) {
        emptyStateGenerator()
    } else {
        listOf(
            EmptyStateListModel(
                emptyStateIconId,
                resources.getString(R.string.empty, missingThingName),
                // retryAction
            )
        )
    }

    data class Config(
        val shouldShow: Boolean,
        @DrawableRes val emptyStateIconId: Int,
        val emptyStateMissingThingName: String,
        val emptyStateGenerator: (() -> List<ListModel>)? = null,
        // val emptyStateRetryAction: (() -> Unit)? = null,
    )
}
