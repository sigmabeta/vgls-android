package com.vgleadsheets.remaster.composers.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.ui.list.ListScreen
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ComposerDetailScreen(
    title: String?,
    listItems: ImmutableList<ListModel>,
    titleUpdater: (String?) -> Unit,
    modifier: Modifier
) {
    ListScreen(
        title = title,
        items = listItems,
        titleUpdater = titleUpdater,
        modifier = modifier,
    )
}
