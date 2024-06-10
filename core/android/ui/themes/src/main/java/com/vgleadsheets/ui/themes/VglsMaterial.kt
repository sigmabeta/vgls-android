@file:Suppress("UnusedPrivateMember")

package com.vgleadsheets.ui.themes

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.ui.colors.VglsDark
import com.vgleadsheets.ui.colors.VglsLight
import com.vgleadsheets.ui.colors.VglsMenu
import com.vgleadsheets.ui.fonts.VglsTypography

@Composable
fun VglsMaterial(
    content: @Composable () -> Unit
) {
    val colors = if (!isSystemInDarkTheme()) {
        VglsLight
    } else {
        VglsDark
    }

    MaterialTheme(
        typography = VglsTypography,
        colorScheme = colors,
        content = content
    )
}

@Composable
fun VglsMaterialMenu(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = VglsTypography,
        colorScheme = VglsMenu,
        content = content
    )
}

@Preview
@Composable
private fun TextPreviewLight() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background
                )
        ) {
            Sample()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TextPreviewDark() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background
                )
        ) {
            Sample()
        }
    }
}

@Composable
private fun Sample() {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SampleText("DisplayLarge", MaterialTheme.typography.displayLarge)
        SampleText("DisplayMedium", MaterialTheme.typography.displayMedium)
        SampleText("DisplaySmall", MaterialTheme.typography.displaySmall)
        SampleText("HeadlineLarge", MaterialTheme.typography.headlineLarge)
        SampleText("HeadlineMedium", MaterialTheme.typography.headlineMedium)
        SampleText("HeadlineSmall", MaterialTheme.typography.headlineSmall)
        SampleText("TitleLarge", MaterialTheme.typography.titleLarge)
        SampleText("TitleMedium", MaterialTheme.typography.titleMedium)
        SampleText("TitleSmall", MaterialTheme.typography.titleSmall)
        SampleText("BodyLarge", MaterialTheme.typography.bodyLarge)
        SampleText("BodyMedium", MaterialTheme.typography.bodyMedium)
        SampleText("BodySmall", MaterialTheme.typography.bodySmall)
        SampleText("LabelLarge", MaterialTheme.typography.labelLarge)
        SampleText("LabelMedium", MaterialTheme.typography.labelMedium)
        SampleText("LabelSmall", MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun SampleText(text: String, style: TextStyle) {
    Text(
        text = text,
        style = style,
        color = MaterialTheme.colorScheme.onBackground,
    )
}
