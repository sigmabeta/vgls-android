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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.themes.VglsMaterial

@Composable
fun ElevatedPill(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = CircleShape
            ),
        content = content
    )
}

@Preview
@Composable
private fun Light() {
    VglsMaterial(useDarkTheme = false) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(4.dp)
        ) {
            Sample()
        }
    }
}

@Preview
@Composable
private fun Dark() {
    VglsMaterial(useDarkTheme = true) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(4.dp)
        ) {
            Sample()
        }
    }
}

@Composable
private fun Sample() {
    ElevatedPill(
        Modifier
            .height(48.dp)
            .width(256.dp)
    ) {
        Flasher()
    }
}
