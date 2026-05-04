package com.vgleadsheets.remaster.browse

import com.vgleadsheets.nav.Destination
import com.vgleadsheets.strings.StringId
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.MenuItemListModel
import net.sigmabeta.sage.components.NoopListModel
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringProvider

data class State(
    val publishDateId: LCE<Long?> = LCE.Uninitialized
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE),
        shouldShowBack = false
    )

    override fun toListItems(stringProvider: StringProvider): List<ListModel> = listOf(
            MenuItemListModel(
                name = stringProvider.getString(StringId.BROWSE_LINK_SHEETS),
                caption = null,
                icon = Icon.DESCRIPTION,
                clickAction = Action.DestinationClicked(Destination.SONGS_LIST.noArgs()),
                selected = false
            ),
            MenuItemListModel(
                name = stringProvider.getString(StringId.BROWSE_LINK_FAVORITES),
                caption = null,
                icon = Icon.FAVORITE_FILLED,
                clickAction = Action.DestinationClicked(Destination.FAVORITES.noArgs()),
                selected = false
            ),
            MenuItemListModel(
                name = stringProvider.getString(StringId.BROWSE_LINK_OFFLINE),
                caption = null,
                icon = Icon.OFFLINE_OUTLINE,
                clickAction = Action.DestinationClicked(Destination.OFFLINE.noArgs()),
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
                name = stringProvider.getString(StringId.BROWSE_LINK_DIFFICULTY),
                caption = null,
                icon = Icon.DIFFICULTY,
                clickAction = Action.DestinationClicked(Destination.DIFFICULTY_LIST.noArgs()),
                selected = false
            ),
            MenuItemListModel(
                name = stringProvider.getString(StringId.BROWSE_LINK_TAG),
                caption = null,
                icon = Icon.TAG,
                clickAction = Action.DestinationClicked(Destination.TAGS_LIST.noArgs()),
                selected = false
            ),
            byPublishDateLink(stringProvider),
        )

    private fun byPublishDateLink(stringProvider: StringProvider): ListModel {
        if (publishDateId is LCE.Content) {
            val id = publishDateId.data

            if (id != null) {
                val name = stringProvider.getString(StringId.BROWSE_LINK_PUBLISH_DATE)
                return MenuItemListModel(
                    name = name,
                    caption = null,
                    icon = Icon.CALENDAR,
                    clickAction = Action.DestinationClicked(Destination.TAGS_VALUES_LIST.forId(id)),
                    selected = false
                )
            }
        }

        return NoopListModel
    }
}
