package com.vgleadsheets.bottombar

import com.vgleadsheets.nav.Destination
import com.vgleadsheets.strings.VglsStringId
import net.sigmabeta.sage.ui.Icon

sealed class NavBarItem(
    val route: String,
    val icon: Icon,
    val labelId: VglsStringId
) {
    data object Home : NavBarItem(Destination.HOME.noArgs(), Icon.HOME, VglsStringId.NAV_LABEL_HOME)
    data object Browse : NavBarItem(Destination.BROWSE.noArgs(), Icon.BROWSE, VglsStringId.NAV_LABEL_BROWSE)
    data object Favorites : NavBarItem(Destination.FAVORITES.noArgs(), Icon.FAVORITE_FILLED, VglsStringId.NAV_LABEL_FAVORITE)
    data object Search : NavBarItem(Destination.SEARCH.noArgs(), Icon.SEARCH, VglsStringId.NAV_LABEL_SEARCH)

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
