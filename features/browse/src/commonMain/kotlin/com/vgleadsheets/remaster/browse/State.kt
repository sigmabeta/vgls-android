package com.vgleadsheets.remaster.browse

import com.vgleadsheets.nav.Destination
import com.vgleadsheets.strings.VglsStringId
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.IconNameListModel
import net.sigmabeta.sage.components.NoopListModel
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringProvider

data class State(
    val publishDateId: LCE<Long?> = LCE.Uninitialized
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(VglsStringId.SCREEN_TITLE_BROWSE),
        shouldShowBack = false
    )

    override fun toListItems(stringProvider: StringProvider): List<ListModel> = listOf(
            IconNameListModel(
                dataId = 0L,
                name = stringProvider.getString(VglsStringId.BROWSE_LINK_SHEETS),
                icon = Icon.Description,
                clickAction = Action.DestinationClicked(Destination.SONGS_LIST.noArgs()),
                active = false
            ),
            IconNameListModel(
                dataId = 1L,
                name = stringProvider.getString(VglsStringId.BROWSE_LINK_FAVORITES),
                icon = Icon.FavoriteFilled,
                clickAction = Action.DestinationClicked(Destination.FAVORITES.noArgs()),
                active = false
            ),
            IconNameListModel(
                dataId = 2L,
                name = stringProvider.getString(VglsStringId.BROWSE_LINK_OFFLINE),
                icon = Icon.OfflineOutline,
                clickAction = Action.DestinationClicked(Destination.OFFLINE.noArgs()),
                active = false
            ),
            IconNameListModel(
                dataId = 3L,
                name = stringProvider.getString(VglsStringId.BROWSE_LINK_GAME),
                icon = Icon.Album,
                clickAction = Action.DestinationClicked(Destination.GAMES_LIST.noArgs()),
                active = false
            ),
            IconNameListModel(
                dataId = 4L,
                name = stringProvider.getString(VglsStringId.BROWSE_LINK_COMPOSER),
                icon = Icon.Person,
                clickAction = Action.DestinationClicked(Destination.COMPOSERS_LIST.noArgs()),
                active = false
            ),
            IconNameListModel(
                dataId = 5L,
                name = stringProvider.getString(VglsStringId.BROWSE_LINK_DIFFICULTY),
                icon = Icon.Difficulty,
                clickAction = Action.DestinationClicked(Destination.DIFFICULTY_LIST.noArgs()),
                active = false
            ),
            IconNameListModel(
                dataId = 6L,
                name = stringProvider.getString(VglsStringId.BROWSE_LINK_TAG),
                icon = Icon.Tag,
                clickAction = Action.DestinationClicked(Destination.TAGS_LIST.noArgs()),
                active = false
            ),
            byPublishDateLink(stringProvider),
        )

    private fun byPublishDateLink(stringProvider: StringProvider): ListModel {
        if (publishDateId is LCE.Content) {
            val id = publishDateId.data

            if (id != null) {
                val name = stringProvider.getString(VglsStringId.BROWSE_LINK_PUBLISH_DATE)
                return IconNameListModel(
                    dataId = 7L,
                    name = name,
                    icon = Icon.Calendar,
                    clickAction = Action.DestinationClicked(Destination.TAGS_VALUES_LIST.forId(id)),
                    active = false
                )
            }
        }

        return NoopListModel
    }
}
