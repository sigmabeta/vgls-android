package com.vgleadsheets.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.vgleadsheets.ui.icons.IcAlbum24dp
import com.vgleadsheets.ui.icons.IcBarChart24dp
import com.vgleadsheets.ui.icons.IcCrossOut24dp
import com.vgleadsheets.ui.icons.IcDescription24dp
import com.vgleadsheets.ui.icons.IcJamFilled
import com.vgleadsheets.ui.icons.IcJamUnfilled
import com.vgleadsheets.ui.icons.IcPlayCircleFilled24
import com.vgleadsheets.ui.icons.IcRemove24dp
import com.vgleadsheets.ui.icons.IcTagBlack24dp

fun Icon.vector(): ImageVector {
    return when (this) {
        Icon.ALBUM -> VglsMaterialVectors.IcAlbum24dp
        Icon.BACK -> Icons.AutoMirrored.Default.ArrowBack
        Icon.BROWSE -> Icons.AutoMirrored.Default.List
        Icon.CALENDAR -> Icons.Default.DateRange
        Icon.CLEAR -> Icons.Default.Clear
        Icon.CROSSOUT -> VglsMaterialVectors.IcCrossOut24dp
        Icon.DESCRIPTION -> VglsMaterialVectors.IcDescription24dp
        Icon.FAVORITE -> VglsMaterialVectors.IcJamFilled
        Icon.FORWARD -> Icons.AutoMirrored.Default.ArrowForward
        Icon.HOME -> Icons.Default.Home
        Icon.JAM_EMPTY -> VglsMaterialVectors.IcJamUnfilled
        Icon.JAM_FILLED -> VglsMaterialVectors.IcJamFilled
        Icon.MINUS -> VglsMaterialVectors.IcRemove24dp
        Icon.PERSON -> Icons.Default.Person
        Icon.PLUS -> Icons.Default.Add
        Icon.REFRESH -> Icons.Default.Refresh
        Icon.DIFFICULTY -> VglsMaterialVectors.IcBarChart24dp
        Icon.TAG -> VglsMaterialVectors.IcTagBlack24dp
        Icon.SEARCH -> Icons.Default.Search
        Icon.SEARCH_YOUTUBE -> VglsMaterialVectors.IcPlayCircleFilled24
        Icon.WARNING -> Icons.Default.Warning
    }
}
