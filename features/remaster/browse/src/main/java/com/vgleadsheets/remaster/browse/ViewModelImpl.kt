package com.vgleadsheets.remaster.browse

import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.ui.icons.R
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.persistentListOf

class ViewModelImpl @AssistedInject constructor(
    @Assisted navigateTo: (String) -> Unit,
) : VglsViewModel<State, Event>(
    initialState = createInitialState(navigateTo)
)

private fun createInitialState(navigateTo: (String) -> Unit) = State(
    items = persistentListOf(
        MenuItemListModel(
            name = "Favorites",
            caption = null,
            iconId = R.drawable.ic_jam_filled,
            onClick = { navigateTo("favorites") },
            selected = false
        ),
        MenuItemListModel(
            name = "By Game",
            caption = null,
            iconId = R.drawable.ic_album_24dp,
            onClick = { navigateTo("games") },
            selected = false
        ),
        MenuItemListModel(
            name = "By Composer",
            caption = null,
            iconId = R.drawable.ic_person_24dp,
            onClick = { navigateTo("composers") },
            selected = false
        ),
        MenuItemListModel(
            name = "By Tag",
            caption = null,
            iconId = R.drawable.ic_tag_black_24dp,
            onClick = { navigateTo("tags") },
            selected = false
        ),
        MenuItemListModel(
            name = "All Sheets",
            caption = null,
            iconId = R.drawable.ic_description_24dp,
            onClick = { navigateTo("sheets") },
            selected = false
        ),
    )
)
