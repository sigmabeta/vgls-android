package com.vgleadsheets.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.themes.VglsMaterial

@Composable
fun ImageNameCaptionListItem(
//    dataId: Long,
    name: String,
    caption: String,
//    imageUrl: String?,
//    @DrawableRes imagePlaceholder: Int,
//    actionableId: Long? = null,
//    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )

        Text(
            text = caption,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun Light() {
    VglsMaterial(useDarkTheme = false) {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Preview
@Composable
fun Dark() {
    VglsMaterial(useDarkTheme = true) {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Composable
private fun Sample() {
    ImageNameCaptionListItem(
//        -1L,
        "Chrono Trigger",
        "Yasunori Mitsuda",
//        "",
//        0,
//        null,
//        {}
    )
}
