package com.vgleadsheets.bottombar

import com.vgleadsheets.nav.Destination
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId

sealed class NavBarItem(
    val route: String,
    val icon: Icon,
    val labelId: StringId
) {
    data object Home : NavBarItem(Destination.HOME.noArgs(), Icon.HOME, StringId.NAV_LABEL_HOME)
    data object Browse : NavBarItem(Destination.BROWSE.noArgs(), Icon.BROWSE, StringId.NAV_LABEL_BROWSE)
    data object Favorites : NavBarItem(Destination.FAVORITES.noArgs(), Icon.FAVORITE, StringId.NAV_LABEL_FAVORITE)
    data object Search : NavBarItem(Destination.SEARCH.noArgs(), Icon.SEARCH, StringId.NAV_LABEL_SEARCH)

    companion object {
        val RAIL_ITEMS = listOf(
            Home,
            Browse,
            Favorites,
            Search,
        )

        val BOTTOM_BAR_ITEMS = listOf(
            Home,
            Browse,
            Search,
        )
    }
}
