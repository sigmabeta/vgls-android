package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.DeviceFontFamilyName
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.themes.VglsMaterial
import com.vgleadsheets.themes.VglsMaterialMenu

@OptIn(ExperimentalTextApi::class)
@Composable
fun SectionHeader(
    name: String,
    menu: Boolean,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val color = if (menu) {
            MaterialTheme.colorScheme.onBackground
        } else {
            MaterialTheme.colorScheme.outline
        }

        Text(
            text = name.uppercase(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily(
                    Font(DeviceFontFamilyName("sans-serif-condensed")),
                )
            ),
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    bottom = 4.dp,
                    end = 32.dp
                )
        )

        // This isn't the end.
        Divider(
            color = color,
            modifier = Modifier
                .padding(
                    end = 8.dp,
                    bottom = 4.dp
                )
        )
    }
}

@Preview
@Composable
private fun Light() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            SampleNotMenu()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dark() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            SampleNotMenu()
        }
    }
}

@Preview
@Composable
private fun Menu() {
    VglsMaterialMenu {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            SampleMenu()
        }
    }
}

@Composable
private fun SampleNotMenu() {
    SectionHeader(
        "Sick new skills",
        true,
        modifier = Modifier
    )
}

@Composable
private fun SampleMenu() {
    SectionHeader(
        "Paths to the future",
        true,
        modifier = Modifier
    )
}
