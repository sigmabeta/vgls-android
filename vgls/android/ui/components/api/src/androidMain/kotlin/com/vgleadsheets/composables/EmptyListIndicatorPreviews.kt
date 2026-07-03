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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vgleadsheets.ui.theme.AppTheme
import net.sigmabeta.sage.components.EmptyStateListModel
import net.sigmabeta.sage.components.ErrorStateListModel
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.vector

@Preview
@Composable
private fun Light() {
    AppTheme {
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
    AppTheme {
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
    AppTheme {
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
    AppTheme {
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
            icon = Icon.Album,
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
        model = ErrorStateListModel(
            failedOperationName = "oops",
            errorString = "Enemy's broken away from me!",
            error = IllegalStateException("Could not maintain aggro. Try using provoke?"),
        ),
        showDebug = true,
        modifier = Modifier
    )
}
