package com.vgleadsheets.nav

import cafe.adriel.voyager.core.screen.Screen

/**
 * A Voyager [Screen] that knows its VGLS route string. Lets [NavViewModel] (which lives below the
 * scaffold module that defines the concrete screens) read the current route off `navigator.lastItem`
 * for its "don't re-navigate to current" and `NavigateSuccessTo` logic. The concrete `VglsScreen`
 * sealed hierarchy in :scaffold implements this.
 */
interface RoutedScreen : Screen {
    val route: String
}
