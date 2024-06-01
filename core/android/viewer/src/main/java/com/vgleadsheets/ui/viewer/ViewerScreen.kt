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
import com.vgleadsheets.components.HeroImageListModel
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.composables.Content
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.themes.VglsMaterial
import kotlinx.collections.immutable.toImmutableList
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

    val title = state.title
    val items = state.listItems

    if (title.title != null) {
        LaunchedEffect(Unit) {
            actionSink.sendAction(VglsAction.Resume)
        }
    }

    val pagerState = rememberPagerState(
        initialPage = state.initialPage
    ) { state.listItems.size }
    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxSize()
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
private fun SampleBigImages() {
    VglsMaterial {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            BigImages()
        }
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

@Composable
fun BigImages() {
    val listItems = List(30) { index ->
        HeroImageListModel(
            sourceInfo = "whatever $index",
            imagePlaceholder = Icon.DESCRIPTION,
            clickAction = VglsAction.Noop,
        )
    }.toImmutableList()

    val source = MutableStateFlow(
        ViewerState(
            listItems = listItems
        )
    )

    ViewerScreen(
        stateSource = source,
        actionSink = { },
        modifier = Modifier
    )
}

@Composable
fun Sheets() {
    val listItems = List(30) { index ->
        SheetPageListModel(
            sourceInfo = "Whatever $index",
            title = "A Trip to Alivel Mall",
            transposition = "C",
            gameName = "Kirby and the Forgotten Land",
            composers = listOf(
                "Hirokazu Ando",
            ).toImmutableList(),
            clickAction = VglsAction.Noop,
        )
    }.toImmutableList()

    val source = MutableStateFlow(
        ViewerState(
            listItems = listItems
        )
    )

    ViewerScreen(
        stateSource = source,
        actionSink = { },
        modifier = Modifier
    )
}
