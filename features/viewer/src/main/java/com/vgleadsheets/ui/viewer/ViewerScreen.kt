@file:OptIn(ExperimentalTelephotoApi::class)

package com.vgleadsheets.ui.viewer

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.bitmaps.SheetConstants
import com.vgleadsheets.components.ZoomableSheetPageListModel
import com.vgleadsheets.composables.ZoomableFullDocItem
import com.vgleadsheets.composables.ZoomableSheetPageItem
import com.vgleadsheets.model.Song
import com.vgleadsheets.pdf.ZOOM_MAX_PDF
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.themes.VglsMaterial
import com.vgleadsheets.ui.vector
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import me.saket.telephoto.ExperimentalTelephotoApi
import me.saket.telephoto.zoomable.Viewport
import me.saket.telephoto.zoomable.ZoomSpec
import me.saket.telephoto.zoomable.ZoomableState
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.spatial.CoordinateSpace
import me.saket.telephoto.zoomable.spatial.SpatialOffset
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.floor

@Composable
@Suppress("LongMethod", "MaxLineLength")
fun ViewerScreen(
    state: ViewerState,
    actionSink: ActionSink,
    showDebug: Boolean,
    modifier: Modifier
) {
    var width by remember { mutableIntStateOf(1) }
    var height by remember { mutableIntStateOf(1) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { actionSink.sendAction(Action.ScreenClicked) }
            .onGloballyPositioned { layoutCoords ->
                width = layoutCoords.size.width
                height = layoutCoords.size.height
            }
    ) {
        val shouldScrollFreely by remember { derivedStateOf { width.toFloat() / height > SheetConstants.ASPECT_RATIO } }
        val items = state.pages()

        if (items.isEmpty()) {
            return
        }

        val singlePage = if (items.size <= 1) 0 else null

        val zoomSpec = ZoomSpec(maxZoomFactor = ZOOM_MAX_PDF.toFloat())

        if (singlePage != null) {
            ZoomableSheetPageItem(
                model = items.first(),
                actionSink = actionSink,
                modifier = Modifier,
                padding = PaddingValues(),
                zoomSpec = zoomSpec,
            )

            if (state.shouldShowLyricsWarning()) {
                LyricsWarning()
            }

            return@Box
        }

        val pagerState = rememberPagerState(
            initialPage = state.initialPage
        ) { items.size }

        val zoomableState = rememberZoomableState(zoomSpec)

        val defaultPageWidth = calculateDefaultPageWidth()
        val scale = zoomableState.contentTransformation.scaleMetadata.userZoom

        val firstVisiblePage = zoomableState.calculateFirstVisiblePage()

        if (shouldScrollFreely) {
            LaunchedEffect(firstVisiblePage) {
                pagerState.scrollToPage(firstVisiblePage)
            }

            if (scale != 0f) {
                LaunchedEffect(state.initialPage) {
                    zoomableState.snapToPage(state.initialPage, defaultPageWidth)
                }
            }

            ZoomableFullDocItem(
                model = items.first(),
                actionSink = actionSink,
                modifier = Modifier,
                padding = PaddingValues(),
                zoomableState = zoomableState,
            )
        } else {
            LaunchedEffect(pagerState.currentPage) {
                zoomableState.snapToPage(pagerState.currentPage, defaultPageWidth)
            }

            SheetPager(
                items = items,
                zoomSpec = zoomSpec,
                pagerState = pagerState,
                allowPaging = !state.isZoomedIn,
                actionSink = actionSink,
            )
        }

        PageControls(zoomableState, pagerState, shouldScrollFreely, items, state, actionSink)

        if (state.shouldShowLyricsWarning()) {
            LyricsWarning()
        }
    }
}

@Composable
private fun BoxScope.PageControls(
    zoomableState: ZoomableState,
    pagerState: PagerState,
    shouldScrollFreely: Boolean,
    items: ImmutableList<ZoomableSheetPageListModel>,
    state: ViewerState,
    actionSink: ActionSink
) {
    if (state.isZoomedIn) {
        return
    }

    val scrollPosition = zoomableState.contentTransformation.offset.x.absoluteValue

    val pageWidth = calculateDefaultPageWidth()
    val pagerPage = pagerState.currentPage

    val prevEnabled = if (shouldScrollFreely) {
        val firstFullVisiblePage = ceil(scrollPosition / pageWidth).toInt()
        firstFullVisiblePage > 0
    } else {
        pagerPage > 0
    }

    val nextEnabled = if (shouldScrollFreely) {
        val rightMostPixel = scrollPosition + getWindowWidth()
        val lastFullVisiblePage = floor(rightMostPixel / pageWidth.toInt()).toInt()

        lastFullVisiblePage <= items.size - 1
    } else {
        pagerPage < items.size - 1
    }

    val visible = state.buttonsVisible

    DirectionButton(
        Action.PrevButtonClicked,
        prevEnabled,
        visible,
        actionSink,
        shouldScrollFreely,
        items.size,
        pagerState,
        zoomableState
    )

    DirectionButton(
        Action.NextButtonClicked,
        nextEnabled,
        visible,
        actionSink,
        shouldScrollFreely,
        items.size,
        pagerState,
        zoomableState
    )
}

@Composable
@Suppress("MagicNumber")
private fun BoxScope.LyricsWarning() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(Color(0, 0, 0, 160))
            .padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = Icon.WARNING.vector(),
            tint = Color.White,
            contentDescription = null,
        )

        Text(
            text = "No lyrics available for this sheet.",
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 8.dp)
                .padding(vertical = 32.dp)
        )
    }
}

@Suppress("ComplexCondition", "LongMethod")
@Composable
private fun BoxScope.DirectionButton(
    action: Action,
    enabled: Boolean,
    visible: Boolean,
    actionSink: ActionSink,
    shouldScrollFreely: Boolean,
    pageCount: Int,
    pagerState: PagerState,
    zoomableState: ZoomableState,
) {
    val (buttonAlignment, imageVector, increment) = when (action) {
        Action.PrevButtonClicked -> Triple(
            Alignment.CenterStart,
            Icon.BACK,
            -1
        )

        Action.NextButtonClicked -> Triple(
            Alignment.CenterEnd,
            Icon.FORWARD,
            1
        )

        else -> throw IllegalArgumentException("Needs to be a direction lol")
    }

    val alpha = if (!visible) ALPHA_TRANSPARENT else if (enabled) ALPHA_ENABLED else ALPHA_DISABLED
    val alphaState by animateFloatAsState(alpha)

    val color = if (enabled) Color.White else Color.Gray
    val colorState by animateColorAsState(color)

    val firstVisiblePage = zoomableState.calculateFirstVisiblePage()
    val defaultPageWidth = calculateDefaultPageWidth()
    var targetPage by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(targetPage) {
        val localTargetPage = targetPage
        if (localTargetPage != null) {
            if (shouldScrollFreely) {
                zoomableState.scrollToPage(localTargetPage, defaultPageWidth)
            } else {
                pagerState.animateScrollToPage(localTargetPage)
            }
            targetPage = null
        }
    }

    val onClick: () -> Unit = {
        actionSink.sendAction(action)
        if (shouldScrollFreely) {
            val result = ((targetPage ?: firstVisiblePage) + increment).coerceIn(0..pageCount)
            targetPage = result
        } else {
            targetPage = pagerState.currentPage + increment
        }
    }

    val dragThresholdPx = with(LocalDensity.current) { 96.dp.toPx() }
    var cumulativeDrag by remember { mutableStateOf(0.0f) }

    val dragState = rememberDraggableState { offset ->
        cumulativeDrag += offset

        if (
            action == Action.PrevButtonClicked && (cumulativeDrag > dragThresholdPx) ||
            action == Action.NextButtonClicked && (cumulativeDrag < -dragThresholdPx)
        ) {
            onClick()
            cumulativeDrag = 0f
        }
    }

    Box(
        modifier = Modifier
            .alpha(alphaState)
            .padding(8.dp)
            .fillMaxWidth(WIDTH_PERCENT_BUTTON)
            .fillMaxHeight(HEIGHT_PERCENT_BUTTON)
            .align(buttonAlignment)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0, 0, 0, ALPHA_BACKGROUND_BUTTON_INT))
            .clickable(enabled = enabled, onClick = onClick)
            .draggable(
                state = dragState,
                orientation = Orientation.Horizontal,
                enabled = enabled,
                onDragStopped = {
                    cumulativeDrag = 0f
                }
            )
    ) {
        Icon(
            imageVector = imageVector.vector(),
            contentDescription = null,
            tint = colorState,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(WIDTH_PERCENT_ICON)
                .aspectRatio(1.0f)
        )
    }
}

private suspend fun ZoomableState.scrollToPage(page: Int, defaultPageWidth: Float) {
    toPage(page, defaultPageWidth, ZoomableState.DefaultPanAnimationSpec)
}

private suspend fun ZoomableState.snapToPage(page: Int, defaultPageWidth: Float) {
    toPage(page, defaultPageWidth, snap())
}

private suspend fun ZoomableState.toPage(page: Int, defaultPageWidth: Float, animationSpec: AnimationSpec<Offset>) {
    val scale = contentTransformation.scaleMetadata.userZoom

    val currentPageWidth = defaultPageWidth * scale

    val currentScrollPosition = contentTransformation.offset.x.absoluteValue
    val targetScrollPosition = page * currentPageWidth
    val diff = targetScrollPosition - currentScrollPosition

    panBy(
        offset = SpatialOffset(
            offset = Offset(-diff, 0f),
            space = CoordinateSpace.Viewport,
        ),
        animationSpec = animationSpec
    )
}

@Composable
private fun ZoomableState.calculateFirstVisiblePage(): Int {
    val defaultPageWidth = calculateDefaultPageWidth()

    val offsetX = contentTransformation.offset.x.absoluteValue
    val scale = contentTransformation.scaleMetadata.userZoom

    val currentPageWidth = defaultPageWidth * scale

    return (offsetX / currentPageWidth).toInt()
}

@Composable
private fun calculateDefaultPageWidth(): Float {
    val windowInfo = LocalWindowInfo.current
    val containerSize = windowInfo.containerSize

    return remember(containerSize) {
        val defaultPageHeight = containerSize.height
        val defaultPageWidth = defaultPageHeight * SheetConstants.ASPECT_RATIO

        defaultPageWidth
    }
}

@Composable
private fun getWindowWidth(): Int {
    val windowInfo = LocalWindowInfo.current
    val containerSize = windowInfo.containerSize

    return containerSize.width
}

private const val WIDTH_PERCENT_ICON = 0.5f

private const val WIDTH_PERCENT_BUTTON = 0.3f
private const val HEIGHT_PERCENT_BUTTON = 0.5f

private const val ALPHA_TRANSPARENT = 0.0f
private const val ALPHA_DISABLED = 0.2f
private const val ALPHA_ENABLED = 1.0f
private const val ALPHA_BACKGROUND_BUTTON = 0.25f
private const val ALPHA_BACKGROUND_BUTTON_INT = (ALPHA_BACKGROUND_BUTTON * 255).toInt()

@Preview
@Composable
private fun SampleOnlyPage() {
    VglsMaterial {
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
    VglsMaterial {
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
    VglsMaterial {
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
    VglsMaterial {
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
    VglsMaterial {
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
    VglsMaterial {
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
    VglsMaterial {
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
        song = Song(
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
        ),
        partApiId = "Bass",
        initialPage = visiblePage,
        buttonsVisible = visible,
        isAltSelected = LCE.Content(false)
    )

    val actionSink: (action: VglsAction) -> Unit = {
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
