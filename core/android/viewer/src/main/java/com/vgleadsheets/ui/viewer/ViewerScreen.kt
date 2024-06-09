package com.vgleadsheets.ui.viewer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.composables.Content
import com.vgleadsheets.model.Song
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewerScreen(
    stateSource: StateFlow<ViewerState>,
    actionSink: ActionSink,
    modifier: Modifier
) {
    val state by stateSource.collectAsStateWithLifecycle()
    val items = state.pages()

    LaunchedEffect(Unit) {
        actionSink.sendAction(VglsAction.Resume)
    }

    if (items.isEmpty()) {
        // To fill the screen and prevent janky animation
        Box(modifier = modifier)
        return
    }

    val pagerState = rememberPagerState(
        initialPage = state.initialPage
    ) { items.size }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .background(Color.Black)
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
private fun SampleSheets() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            Sheets()
        }
    }
}

@Suppress("MagicNumber")
@Composable
fun Sheets() {
    val source = MutableStateFlow(
        ViewerState(
            song = Song(
                1234L,
                name = "A Trip to Alivel Mall",
                gameName = "Kirby and the Forgotten Land",
                hasVocals = false,
                pageCount = 3,
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
            initialPage = 0
        )
    )

    ViewerScreen(
        stateSource = source,
        actionSink = { },
        modifier = Modifier.fillMaxSize()
    )
}
