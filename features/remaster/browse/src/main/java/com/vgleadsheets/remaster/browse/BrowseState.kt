package com.vgleadsheets.remaster.browse

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.MenuItemListModel

data class BrowseState(
    val items: List<ListModel> = listOf(
        MenuItemListModel(
            name = "Favorites",
            caption = null,
            iconId = com.vgleadsheets.ui.icons.R.drawable.ic_jam_filled,
            onClick = {},
            selected = false
        ),
        MenuItemListModel(
            name = "By Game",
            caption = null,
            iconId = com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp,
            onClick = {},
            selected = false
        ),
        MenuItemListModel(
            name = "By Composer",
            caption = null,
            iconId = com.vgleadsheets.ui.icons.R.drawable.ic_person_24dp,
            onClick = {},
            selected = false
        ),
        MenuItemListModel(
            name = "By Tag",
            caption = null,
            iconId = com.vgleadsheets.ui.icons.R.drawable.ic_tag_black_24dp,
            onClick = {},
            selected = false
        ),
        MenuItemListModel(
            name = "All Sheets",
            caption = null,
            iconId = com.vgleadsheets.ui.icons.R.drawable.ic_description_24dp,
            onClick = {},
            selected = false
        ),
    )
)
