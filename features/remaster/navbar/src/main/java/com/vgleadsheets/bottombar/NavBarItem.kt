package com.vgleadsheets.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavBarItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    Home("home", Icons.Default.Home, "Home"),
    Browse("browse", Icons.AutoMirrored.Default.List, "Browse"),
    Search("search", Icons.Default.Search, "Search"),
}
