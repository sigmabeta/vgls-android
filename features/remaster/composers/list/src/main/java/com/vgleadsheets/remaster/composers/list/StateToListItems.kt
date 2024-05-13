package com.vgleadsheets.remaster.composers.list

import android.content.res.Resources
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.ui.Icon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

fun State.toListItems(
    resources: Resources,
    onComposerClick: (Long) -> Unit,
): ImmutableList<ListModel> {
    return composers
        .map { composer ->
            WideItemListModel(
                dataId = composer.id,
                name = composer.name,
                imageUrl = composer.photoUrl,
                imagePlaceholder = Icon.PERSON,
                onClick = { onComposerClick(composer.id) }
            )
        }
        .toPersistentList()
}
