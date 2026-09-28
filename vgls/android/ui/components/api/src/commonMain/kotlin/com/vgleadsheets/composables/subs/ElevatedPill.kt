package com.vgleadsheets.composables.subs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.vgleadsheets.ui.theme.AppTheme

@Composable
fun ElevatedPill(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val shape = CircleShape
    val actualModifier = if (platformSupportsElevationShadow()) {
        modifier.shadow(
            elevation = 4.dp,
            shape = shape
        )
    } else {
        modifier.clip(shape)
    }

    Surface(
        modifier = actualModifier,
        content = content
    )
}
