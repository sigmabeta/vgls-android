package com.vgleadsheets.remaster.games.list

import android.content.res.Resources
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.ui.icons.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

fun State.toListItems(
    resources: Resources,
    onGameClick: (Long) -> Unit,
): ImmutableList<ListModel> {
    return games
        .map { game ->
            SquareItemListModel(
                dataId = game.id,
                name = game.name,
                imageUrl = game.photoUrl,
                imagePlaceholder = R.drawable.ic_album_24dp,
                onClick = { onGameClick(game.id) }
            )
        }
        .toPersistentList()
}
