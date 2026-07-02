package com.vgleadsheets.composables.subs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.ui.theme.AppTheme
import com.vgleadsheets.ui.theme.AppThemeMenu
import net.sigmabeta.sage.ui.vector

@Composable
@Suppress("MagicNumber")
fun Rating(
    score: Int,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
    ) {
        for (index in 1..4) {
            val icon = if (score >= index) {
                net.sigmabeta.sage.ui.Icon.FavoriteFilled
            } else {
                net.sigmabeta.sage.ui.Icon.FavoriteEmpty
            }

            Icon(
                imageVector = icon.vector(),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }
    }
}
