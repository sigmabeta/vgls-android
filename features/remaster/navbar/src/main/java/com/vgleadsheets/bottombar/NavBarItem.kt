package com.vgleadsheets.bottombar

import com.vgleadsheets.nav.Destination
import com.vgleadsheets.ui.Icon

sealed class NavBarItem(
    val route: String,
    val icon: Icon,
    val label: String
) {
    data object Home : NavBarItem(Destination.HOME.noArgs(), Icon.HOME, "Home")
    data object Browse : NavBarItem(Destination.BROWSE.noArgs(), Icon.BROWSE, "Browse")
    data object Favorites : NavBarItem(Destination.FAVORITES.noArgs(), Icon.FAVORITE, "Favorites")
    data object Search : NavBarItem(Destination.SEARCH.noArgs(), Icon.SEARCH, "Search")

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

