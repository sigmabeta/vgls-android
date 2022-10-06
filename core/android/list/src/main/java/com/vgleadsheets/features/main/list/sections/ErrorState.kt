package com.vgleadsheets.features.main.list.sections

import android.content.res.Resources
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.list.R

object ErrorState {
    fun listItems(
        shouldShow: Boolean,
        shouldShowDeveloperMessage: Boolean,
        failedOperationName: String,
        messageForDeveloper: String,
        resources: Resources,
        messageForUser: String? = null,
        errorStateGenerator: (() -> List<ListModel>)? = null,
        // retryAction: (() -> Unit)? = null,
    ) = if (!shouldShow) {
        emptyList()
    } else if (errorStateGenerator != null) {
        errorStateGenerator()
    } else {
        listOf(
            ErrorStateListModel(
                failedOperationName,
                messageToShow(
                    shouldShowDeveloperMessage,
                    messageForUser,
                    messageForDeveloper,
                    resources
                ),
                // retryAction
            )
        )
    }

    @Suppress("IfThenToElvis")
    private fun messageToShow(
        shouldShowDeveloperMessage: Boolean,
        messageForUser: String?,
        messageForDeveloper: String,
        resources: Resources
    ) = if (shouldShowDeveloperMessage) {
        messageForDeveloper
    } else if (messageForUser != null) {
        messageForUser
    } else {
        resources.getString(R.string.error)
    }

    data class Config(
        val shouldShow: Boolean,
        val shouldShowDeveloperMessage: Boolean,
        val failedOperationName: String,
        val messageForDeveloper: String,
        val messageForUser: String? = null,
        val errorStateGenerator: (() -> List<ListModel>)? = null,
        // val errorRetryAction: (() -> Unit)? = null,
    )
}
