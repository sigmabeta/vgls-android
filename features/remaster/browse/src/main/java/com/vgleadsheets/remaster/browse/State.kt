package com.vgleadsheets.remaster.browse

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data object State : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE),
        shouldShowBack = false
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> = persistentListOf(
        MenuItemListModel(
            name = stringProvider.getString(StringId.BROWSE_LINK_FAVORITES),
            caption = null,
            icon = Icon.JAM_FILLED,
            clickAction = Action.DestinationClicked(Destination.FAVORITES.noArgs()),
            selected = false
        ),
        MenuItemListModel(
            name = stringProvider.getString(StringId.BROWSE_LINK_GAME),
            caption = null,
            icon = Icon.ALBUM,
            clickAction = Action.DestinationClicked(Destination.GAMES_LIST.noArgs()),
            selected = false
        ),
        MenuItemListModel(
            name = stringProvider.getString(StringId.BROWSE_LINK_COMPOSER),
            caption = null,
            icon = Icon.PERSON,
            clickAction = Action.DestinationClicked(Destination.COMPOSERS_LIST.noArgs()),
            selected = false
        ),
        MenuItemListModel(
            name = stringProvider.getString(StringId.BROWSE_LINK_TAG),
            caption = null,
            icon = Icon.TAG,
            clickAction = Action.DestinationClicked(Destination.TAGS_LIST.noArgs()),
            selected = false
        ),
        MenuItemListModel(
            name = stringProvider.getString(StringId.BROWSE_LINK_SHEETS),
            caption = null,
            icon = Icon.DESCRIPTION,
            clickAction = Action.DestinationClicked(Destination.SONGS_LIST.noArgs()),
            selected = false
        ),
    )
}
