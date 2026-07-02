package com.vgleadsheets.bottombar

import com.vgleadsheets.nav.Destination
import com.vgleadsheets.strings.VglsStringId
import net.sigmabeta.sage.ui.Icon

sealed class NavBarItem(
    val route: String,
    val icon: Icon,
    val labelId: VglsStringId
) {
    data object Home : NavBarItem(Destination.HOME.noArgs(), Icon.Home, VglsStringId.NAV_LABEL_HOME)
    data object Browse : NavBarItem(Destination.BROWSE.noArgs(), Icon.Browse, VglsStringId.NAV_LABEL_BROWSE)
    data object Favorites : NavBarItem(Destination.FAVORITES.noArgs(), Icon.FavoriteFilled, VglsStringId.NAV_LABEL_FAVORITE)
    data object Search : NavBarItem(Destination.SEARCH.noArgs(), Icon.Search, VglsStringId.NAV_LABEL_SEARCH)

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
