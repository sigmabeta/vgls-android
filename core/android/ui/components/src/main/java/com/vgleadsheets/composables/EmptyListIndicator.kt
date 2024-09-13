package com.vgleadsheets.composables

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
        error = model.error,
        showDebug = BuildConfig.DEBUG,
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
        modifier = modifier
    )
}

@Composable
@Suppress("LongMethod")
private fun EmptyListIndicator(
    explanation: String,
    iconId: Int,
    showCrossOut: Boolean,
    showDebug: Boolean = false,
    error: Throwable? = null,
    modifier: Modifier,
) {
    val color = MaterialTheme.colorScheme.outline
    var showDetails by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .clickable(enabled = showDebug) { showDetails = !showDetails }
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 8.dp
                )
        ) {
            val crossOutResource = com.vgleadsheets.ui.icons.R.drawable.ic_cross_out_24dp

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

        if (showDebug && error != null) {
            val firstStackElement = error.stackTrace.first()
            val className = firstStackElement.fileName
            val methodName = firstStackElement.methodName
            val lineNumber = firstStackElement.lineNumber
            val summary = "$className: $lineNumber ($methodName)"
            DebugText(summary, color)

            AnimatedVisibility(visible = !showDetails) {
                val message = error.message
                if (message != null) {
                    DebugTextSmall(message, color)
                }
            }

            AnimatedVisibility(visible = showDetails) {
                DebugTextSmall(error.stackTraceToString(), color)
            }
        }
    }
}

@Composable
private fun DebugText(debugText: String, color: Color) {
    Text(
        text = debugText,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
        ),
        color = color,
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .padding(bottom = 8.dp)
            .widthIn(min = 200.dp, max = 400.dp)
    )
}

@Composable
@Suppress("MagicNumber")
private fun DebugTextSmall(debugText: String, color: Color) {
    Text(
        text = debugText,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 6.sp,
            fontFamily = FontFamily.Monospace
        ),
        color = color,
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .padding(bottom = 16.dp)
            .widthIn(min = 200.dp, max = 400.dp)
            .alpha(0.8f)
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
            SampleError()
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
            SampleError()
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
private fun SampleError() {
    EmptyListIndicator(
        ErrorStateListModel(
            failedOperationName = "oops",
            errorString = "Enemy's broken away from me!",
            error = IllegalStateException("Could not maintain aggro. Try using provoke?"),
        ),
        Modifier
    )
}
