package com.vgleadsheets.ui.viewer

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.vgleadsheets.composables.EmptyListIndicator
import com.vgleadsheets.composables.ZoomableFullDocItem
import com.vgleadsheets.composables.ZoomableSheetPageItem
import com.vgleadsheets.model.Song
import com.vgleadsheets.pdf.ZOOM_MAX_PDF
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import me.saket.telephoto.ExperimentalTelephotoApi
import me.saket.telephoto.zoomable.Viewport
import me.saket.telephoto.zoomable.ZoomSpec
import me.saket.telephoto.zoomable.ZoomableState
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.spatial.CoordinateSpace
import me.saket.telephoto.zoomable.spatial.SpatialOffset
import com.vgleadsheets.bitmaps.SheetConstants
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.components.ZoomableSheetPageListModel
import net.sigmabeta.sage.ui.Icon
import com.vgleadsheets.ui.theme.AppTheme
import net.sigmabeta.sage.ui.vector
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.floor

@Preview
@Composable
private fun SampleOnlyPage() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            Sheets(0, 1)
        }
    }
}

@Preview
@Composable
private fun SampleFirstPage() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            Sheets(0, 3)
        }
    }
}

@Preview
@Composable
private fun SampleSecondPage() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            Sheets(1, 3)
        }
    }
}

@Preview
@Composable
private fun SampleLastPage() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            Sheets(2, 3)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SampleDark() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            Sheets(0, 3)
        }
    }
}

@Preview(
    widthDp = 800,
    heightDp = 400
)
@Composable
private fun SampleWide() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            Sheets(0, 2)
        }
    }
}

@Preview(
    widthDp = 720,
    heightDp = 800
)
@Composable
private fun SampleWeirdSize() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            Sheets(0, 2)
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun Sheets(
    visiblePage: Int,
    pageCount: Int,
) {
    var visible by remember { mutableStateOf(true) }

    val state = ViewerState(
        song = LCE.Content(
            Song(
                1234L,
                name = "A Trip to Alivel Mall",
                gameName = "Kirby and the Forgotten Land",
                hasVocals = false,
                pageCount = pageCount,
                composers = emptyList(),
                filename = "Whatever",
                isAvailableOffline = true,
                lyricPageCount = 4,
                playCount = 1234,
                game = null,
                gameId = 123435L,
                altPageCount = 5,
                isAltSelected = false,
                isFavorite = true,
                lastModifiedOnServer = 0L,
                lastDownloaded = 0L,
            )
        ),
        partApiId = "Bass",
        initialPage = visiblePage,
        buttonsVisible = visible,
        isAltSelected = LCE.Content(false)
    )

    val actionSink: (action: SageAction) -> Unit = {
        when (it) {
            Action.PrevButtonClicked, Action.NextButtonClicked -> {
                visible = true
            }
        }
    }

    if (visible) {
        LaunchedEffect(visible) {
            delay(1_000L)
            visible = false
        }
    }

    ViewerScreen(
        state = state,
        actionSink = actionSink,
        showDebug = true,
        modifier = Modifier.fillMaxSize()
    )
}
