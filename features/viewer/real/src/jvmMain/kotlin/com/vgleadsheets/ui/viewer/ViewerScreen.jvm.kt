package com.vgleadsheets.ui.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import com.vgleadsheets.bitmaps.SheetConstants
import com.vgleadsheets.composables.EmptyListIndicator
import com.vgleadsheets.composables.subs.CrossfadeSheet
import com.vgleadsheets.images.LoadingIndicatorConfig
import kotlinx.coroutines.launch
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.vector

/**
 * Desktop sheet viewer. Unlike the android actual (telephoto pinch-zoom over an
 * [android.graphics.pdf.PdfRenderer] bitmap), this renders each page with the shared [CrossfadeSheet]
 * — whose Coil request is served by the JVM PDFBox decoder — inside a plain [HorizontalPager]. Zoom
 * is intentionally omitted for now; navigation is by on-screen arrows, mouse drag, or arrow keys.
 */
@Composable
actual fun ViewerScreen(
    state: ViewerState,
    actionSink: ActionSink,
    showDebug: Boolean,
    modifier: Modifier,
) {
    val error = state.error()
    if (error.isNotEmpty()) {
        EmptyListIndicator(
            model = error.first(),
            modifier = modifier,
            showDebug = showDebug,
            onBlack = true,
        )
        return
    }

    val items = state.pages()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { actionSink.sendAction(Action.ScreenClicked) },
    ) {
        if (items.isEmpty()) {
            return@Box
        }

        val pagerState = rememberPagerState(
            initialPage = state.initialPage.coerceIn(0, items.size - 1),
        ) { items.size }
        val scope = rememberCoroutineScope()

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val item = items[page]
            // Center a single sheet, sized to the page aspect ratio, so the black Box shows around it
            // (the sheet's own white/beige background comes from CrossfadeSheet, not from filling the
            // whole screen).
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                CrossfadeSheet(
                    pdfConfigById = item.pdfConfigById,
                    contentDescription = "${item.title} from ${item.gameName}, page ${item.pageNumber + 1}",
                    loadingIndicatorConfig = LoadingIndicatorConfig(
                        title = item.title,
                        gameName = item.gameName,
                        composers = item.composers,
                        pageNumber = item.pageNumber,
                        loaderSize = item.pdfConfigById.pdfSize,
                    ),
                    sheetId = item.pdfConfigById.songId,
                    showDebug = showDebug,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(SheetConstants.ASPECT_RATIO),
                )
            }
        }

        val goToPage: (Int) -> Unit = { target ->
            scope.launch { pagerState.animateScrollToPage(target.coerceIn(0, items.size - 1)) }
        }
        val onPrevious = {
            actionSink.sendAction(Action.LeftArrowPressed)
            goToPage(pagerState.currentPage - 1)
        }
        val onNext = {
            actionSink.sendAction(Action.RightArrowPressed)
            goToPage(pagerState.currentPage + 1)
        }

        if (items.size > 1 && state.buttonsVisible) {
            DirectionButton(
                alignment = Alignment.CenterStart,
                icon = Icon.Back,
                enabled = pagerState.currentPage > 0,
                onClick = onPrevious,
            )
            DirectionButton(
                alignment = Alignment.CenterEnd,
                icon = Icon.Forward,
                enabled = pagerState.currentPage < items.size - 1,
                onClick = onNext,
            )
        }

        ArrowKeyHandler(
            onPrevious = onPrevious,
            onNext = onNext,
            onBack = { actionSink.sendAction(SageAction.DeviceBack) },
        )
    }
}

@Composable
private fun ArrowKeyHandler(
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Box(
        modifier = Modifier
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type != KeyEventType.KeyDown) return@onKeyEvent false
                when (keyEvent.key) {
                    Key.DirectionLeft -> {
                        onPrevious()
                        true
                    }

                    Key.DirectionRight -> {
                        onNext()
                        true
                    }

                    Key.Back, Key.Escape -> {
                        onBack()
                        true
                    }

                    else -> false
                }
            },
    )
}

@Suppress("MagicNumber")
@Composable
private fun BoxScope.DirectionButton(
    alignment: Alignment,
    icon: Icon,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val alpha = if (enabled) 1.0f else 0.2f
    val tint = if (enabled) Color.White else Color.Gray

    Box(
        modifier = Modifier
            .alpha(alpha)
            .padding(8.dp)
            .fillMaxWidth(WIDTH_PERCENT_BUTTON)
            .fillMaxHeight(HEIGHT_PERCENT_BUTTON)
            .align(alignment)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0, 0, 0, ALPHA_BACKGROUND_BUTTON_INT))
            .clickable(enabled = enabled, onClick = onClick),
    ) {
        Icon(
            imageVector = icon.vector(),
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(WIDTH_PERCENT_ICON)
                .aspectRatio(1.0f),
        )
    }
}

private const val WIDTH_PERCENT_ICON = 0.5f
private const val WIDTH_PERCENT_BUTTON = 0.3f
private const val HEIGHT_PERCENT_BUTTON = 0.5f
private const val ALPHA_BACKGROUND_BUTTON_INT = 64
