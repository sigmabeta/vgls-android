package com.vgleadsheets.scaffold

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.bottombar.BottomBarState
import com.vgleadsheets.bottombar.BottomBarVisibility
import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ImageNameListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.components.WideItemListModel
import com.vgleadsheets.composables.HorizontalScroller
import com.vgleadsheets.composables.ImageNameListItem
import com.vgleadsheets.composables.SectionHeader
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.model.Song
import com.vgleadsheets.topbar.TopBarState
import com.vgleadsheets.topbar.TopBarVisibility
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.themes.VglsMaterial
import com.vgleadsheets.ui.viewer.Action
import com.vgleadsheets.ui.viewer.ViewerScreen
import com.vgleadsheets.ui.viewer.ViewerState
import kotlinx.collections.immutable.toImmutableList
import java.util.Random

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun AppPreviewRegular() {
    VglsMaterial {
        SampleScreen { _, _ ->
            SampleScroller()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppPreviewDark() {
    VglsMaterial {
        SampleScreen { _, _ ->
            SampleScroller()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ViewerPreviewRegular() {
    VglsMaterial {
        SampleScreen { _, eventDispatcher ->
            SampleSheets(eventDispatcher)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ViewerPreviewDark() {
    VglsMaterial {
        SampleScreen { _, eventDispatcher ->
            SampleSheets(eventDispatcher)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SampleScreen(screenContent: @Composable (PaddingValues, EventDispatcher) -> Unit) {
    val navController = rememberNavController()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)
    val snackbarHostState = remember { SnackbarHostState() }

    var topBarVisibility by remember { mutableStateOf(TopBarVisibility.VISIBLE) }
    var bottomBarVisibility by remember { mutableStateOf(BottomBarVisibility.VISIBLE) }

    val topBarVmState = TopBarState(
        TitleBarModel(),
        visibility = topBarVisibility
    )
    val bottomBarVmState = BottomBarState(
        visibility = bottomBarVisibility
    )

    val toggleBarVisibility = {
        topBarVisibility = if (topBarVisibility == TopBarVisibility.VISIBLE) {
            TopBarVisibility.HIDDEN
        } else {
            TopBarVisibility.VISIBLE
        }

        bottomBarVisibility = if (bottomBarVisibility == BottomBarVisibility.VISIBLE) {
            BottomBarVisibility.HIDDEN
        } else {
            BottomBarVisibility.VISIBLE
        }
    }

    val eventDispatcher = object : EventDispatcher {
        override fun addEventSink(sink: EventSink) = Unit
        override fun removeEventSink(sink: EventSink) = Unit
        override val sendEvent = { event: VglsEvent ->
            if (event is VglsEvent.ShowUiChrome) {
                toggleBarVisibility()
            }
        }
    }

    AppContent(
        navController = navController,
        scrollBehavior = scrollBehavior,
        snackbarHostState = snackbarHostState,
        topBarVmState = topBarVmState,
        topBarActionHandler = { },
        bottomBarVmState = bottomBarVmState,
        mainContent = { padding -> screenContent(padding, eventDispatcher) },
        modifier = Modifier,
    )
}

@Composable
@Suppress("MagicNumber")
private fun SampleScroller() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        val rng = Random("HorizontalScroller".hashCode().toLong())

        val paddingModifier = Modifier.padding(
            horizontal = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.margin_side)
        )
        SquareItemSection(rng, paddingModifier)
        WideItemSection(rng, paddingModifier)
        VerticalSection(rng, paddingModifier)
        SquareItemSection(rng, paddingModifier)
        VerticalSection(rng, paddingModifier)
    }
}

@Composable
@Suppress("MagicNumber")
private fun SquareItemSection(rng: Random, modifier: Modifier) {
    SectionHeader(
        name = "Square Items",
        modifier = modifier
    )

    HorizontalScroller(
        model = HorizontalScrollerListModel(
            dataId = 1_000_000L,
            scrollingItems = List(15) { index ->
                SquareItemListModel(
                    dataId = index.toLong(),
                    name = "Square #$index",
                    sourceInfo = rng.nextInt().toString(),
                    imagePlaceholder = Icon.ALBUM,
                    null,
                    clickAction = VglsAction.Noop,
                )
            }.toImmutableList()
        ),
        PreviewActionSink { },
        modifier = Modifier,
    )
}

@Composable
@Suppress("MagicNumber")
private fun WideItemSection(rng: Random, modifier: Modifier) {
    SectionHeader(
        name = "Wide Items",
        modifier = modifier
    )

    HorizontalScroller(
        model = HorizontalScrollerListModel(
            dataId = 1_000L,
            scrollingItems = List(15) { index ->
                WideItemListModel(
                    dataId = index.toLong(),
                    name = "Wide Item #$index",
                    sourceInfo = rng.nextInt().toString(),
                    Icon.PERSON,
                    null,
                    clickAction = VglsAction.Noop
                )
            }.toImmutableList()
        ),
        PreviewActionSink { },
        modifier = Modifier,
    )
}

@Composable
@Suppress("MagicNumber")
private fun VerticalSection(rng: Random, modifier: Modifier) {
    SectionHeader(
        name = "Vertically Scrolling Items",
        modifier = modifier
    )

    repeat(3) { index ->
        ImageNameListItem(
            model = ImageNameListModel(
                dataId = index.toLong(),
                name = "Wide Item #$index",
                sourceInfo = rng.nextInt().toString(),
                Icon.DESCRIPTION,
                null,
                clickAction = VglsAction.Noop
            ),
            PreviewActionSink { },
            modifier = Modifier,
        )
    }
}

@Composable
private fun SampleSheets(eventDispatcher: EventDispatcher) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)
            .fillMaxSize()
    ) {
        Sheets(eventDispatcher)
    }
}

@Suppress("MagicNumber")
@Composable
private fun Sheets(
    eventDispatcher: EventDispatcher,
) {
    val state = ViewerState(
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

    val actionSink: (VglsAction) -> Unit = {
        when (it) {
            is Action.PageClicked -> {
                eventDispatcher.sendEvent(VglsEvent.ShowUiChrome)
            }
        }
    }

    ViewerScreen(
        state = state,
        actionSink = actionSink,
        modifier = Modifier
    )
}
