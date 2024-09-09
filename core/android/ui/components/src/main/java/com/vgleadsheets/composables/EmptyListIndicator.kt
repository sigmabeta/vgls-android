package com.vgleadsheets.composables

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.ui.components.BuildConfig
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun EmptyListIndicator(
    model: ErrorStateListModel,
    modifier: Modifier,
    ) {
    EmptyListIndicator(
        explanation = model.errorString,
        iconId = com.vgleadsheets.ui.icons.R.drawable.ic_error_24dp,
        showCrossOut = false,
        menu = false,
        debugText = model.debugText,
        modifier = modifier,
    )
}

@Composable
fun EmptyListIndicator(
    model: EmptyStateListModel,
    modifier: Modifier,
) {
    EmptyListIndicator(
        explanation = model.explanation,
        iconId = model.iconId,
        showCrossOut = model.showCrossOut,
        menu = false,
        debugText = model.debugText,
        modifier = modifier
    )
}

@Composable
private fun EmptyListIndicator(
    explanation: String,
    iconId: Int,
    showCrossOut: Boolean,
    menu: Boolean,
    showDebug: Boolean = BuildConfig.DEBUG,
    debugText: String?,
    modifier: Modifier,
) {
    val color = if (menu) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.outline
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 8.dp
                )
        ) {
            val crossOutResource = if (menu) {
                com.vgleadsheets.ui.icons.R.drawable.ic_cross_out_menu_24dp
            } else {
                com.vgleadsheets.ui.icons.R.drawable.ic_cross_out_24dp
            }

            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(96.dp)
            )

            if (showCrossOut) {
                Icon(
                    painter = painterResource(id = crossOutResource),
                    tint = Color.Unspecified,
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                )
            }
        }

        Text(
            text = explanation,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = color,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(bottom = 16.dp)
                .widthIn(min = 200.dp, max = 400.dp)
        )

        if (showDebug && debugText != null) {
            DebugText(debugText, color)
        }
    }
}

@Composable
private fun DebugText(debugText: String, color: Color) {
    Text(
        text = debugText,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace
        ),
        color = color,
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .padding(bottom = 16.dp)
            .widthIn(min = 200.dp, max = 400.dp)
    )
}

@Preview
@Composable
private fun Light() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun Dark() {
    VglsMaterial {
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
private fun LightError() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleErrorNotMenu()
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DarkError() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleErrorNotMenu()
        }
    }
}

@Composable
private fun Sample() {
    EmptyListIndicator(
        EmptyStateListModel(
            iconId = com.vgleadsheets.ui.icons.R.drawable.ic_album_24dp,
            explanation = "It's all part of the protocol, innit?",
            debugText = null,
            showCrossOut = true
        ),
        Modifier
    )
}

@Composable
private fun SampleErrorNotMenu() {
    EmptyListIndicator(
        ErrorStateListModel(
            failedOperationName = "oops",
            errorString = "Enemy's broken away from me!",
            debugText = "java.lang.IllegalStateException: Failed to allocate bitmap."
        ),
        Modifier
    )
}
