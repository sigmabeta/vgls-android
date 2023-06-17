package com.vgleadsheets.composables.subs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ElevatedRoundRect(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(cornerRadius)
            ),
        content = content
    )
}
