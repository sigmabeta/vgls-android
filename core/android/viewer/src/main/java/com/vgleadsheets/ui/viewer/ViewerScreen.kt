package com.vgleadsheets.ui.viewer

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.composables.Content
import com.vgleadsheets.model.Song
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewerScreen(
    state: ViewerState,
    actionSink: ActionSink,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        val items = state.pages()

        val pagerState = rememberPagerState(
            initialPage = state.initialPage
        ) { items.size }

        SheetPager(
            items,
            pagerState,
            actionSink
        )

        if (items.size > 1) {
            val currentPage = pagerState.currentPage

            val prevEnabled = currentPage > 0
            val nextEnabled = currentPage < items.size - 1

            val visible = state.buttonsVisible

            DirectionButton(Action.PrevButtonClicked, prevEnabled, visible, actionSink, pagerState)
            DirectionButton(Action.NextButtonClicked, nextEnabled, visible, actionSink, pagerState)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.DirectionButton(
    action: Action,
    enabled: Boolean,
    visible: Boolean,
    actionSink: ActionSink,
    pagerState: PagerState,
) {
    val (buttonAlignment, imageVector, increment) = when (action) {
        Action.PrevButtonClicked -> Triple(
            Alignment.CenterStart,
            Icons.Default.ArrowBack,
            -1
        )

        Action.NextButtonClicked -> Triple(
            Alignment.CenterEnd,
            Icons.Default.ArrowForward,
            1
        )

        else -> throw IllegalArgumentException("Needs to be a direction lol")
    }

    val alpha = if (!visible) 0.0f else if (enabled) 1.0f else 0.2f
    val alphaState by animateFloatAsState(alpha)

    val color = if (enabled) Color.White else Color.Gray
    val colorState by animateColorAsState(color)

    val scrollScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .alpha(alphaState)
            .padding(8.dp)
            .fillMaxWidth(0.3f)
            .fillMaxHeight(0.5f)
            .align(buttonAlignment)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0, 0, 0, 64))
            .clickable(enabled = enabled) {
                actionSink.sendAction(action)
                scrollScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + increment)
                }
            }
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = colorState,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.5f)
                .aspectRatio(1.0f)
        )
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun BoxScope.SheetPager(
    items: ImmutableList<SheetPageListModel>,
    pagerState: PagerState,
    actionSink: ActionSink,
) {
    LaunchedEffect(Unit) {
        actionSink.sendAction(VglsAction.Resume)
    }

    if (items.isEmpty()) {
        // To fill the screen and prevent janky animation
        Box(modifier = Modifier)
        return
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.align(Alignment.Center)
    ) { page ->
        val item = items[page]
        item.Content(
            actionSink = actionSink,
            modifier = Modifier
        )
    }
}

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
        buttonsVisible = visible
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
        modifier = Modifier.fillMaxSize()
    )
}

