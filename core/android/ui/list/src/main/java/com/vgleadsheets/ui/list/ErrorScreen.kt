package com.vgleadsheets.ui.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.composables.Content
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.id
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ErrorScreen(
    errorString: String,
    modifier: Modifier = Modifier
) {
    val errorMessage = buildString {
        append(stringResource(StringId.ERROR_BROKEN_SCREEN_DESC.id()))
        if (BuildConfig.DEBUG) {
            append("\n")
            append("\n")
            append("Error details: ")
            append(errorString)
        }
    }
    val items = persistentListOf(
        ErrorStateListModel(
            failedOperationName = "renderScreen",
            errorString = errorMessage
        )
    )

    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = items,
            key = { it.dataId },
            contentType = { it.layoutId() }
        ) {
            it.Content(
                sink = { },
                mod = Modifier.animateItem(),
                pad = PaddingValues(horizontal = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.margin_side))
            )
        }
    }
}
