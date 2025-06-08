package com.vgleadsheets.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
@Suppress("MagicNumber")
fun PdfTestScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        var zoom by remember { mutableFloatStateOf(1f) }
        var zoomTemporary by remember { mutableFloatStateOf(1f) }

        val (id, pageNumber) = 405L to null as Int? // Multipage
        // val (id, pageNumber) = 603L to 0 as Int? // Multipage

        // ZoomableSheet(
        //     pdfConfigById = PdfConfigById(
        //         songId = id,
        //         pageNumber = pageNumber,
        //         isAltSelected = false,
        //         pdfSize = PdfSize.FILL,
        //     ),
        //     contentDescription = null,
        //     loadingIndicatorConfig = LoadingIndicatorConfig(
        //         title = "Song",
        //         gameName = "Game",
        //         composers = persistentListOf(),
        //         pageNumber = 0,
        //         loaderSize = PdfSize.FILL,
        //     ),
        //     actionSink = { },
        //     modifier = Modifier.fillMaxSize()
        // )

        // ZoomableFullDocItem(
        //     model = ZoomableSheetPageListModel(),
        //     actionSink = TODO(),
        //     modifier = TODO(),
        //     padding = TODO()
        // )

        // Information view
        // val bottomInset = WindowInsets.navigationBars.asPaddingValues()
        // Column(
        //     modifier = Modifier
        //         .alpha(0.8f)
        //         .background(MaterialTheme.colorScheme.background)
        //         .padding(top = 8.dp)
        //         .padding(bottom = bottomInset.calculateBottomPadding())
        //         .padding(horizontal = 16.dp)
        //         .fillMaxWidth()
        //         .align(Alignment.BottomCenter)
        // ) {
        //     Slider(
        //         value = zoomTemporary,
        //         valueRange = 0.5f .. 4f,
        //         onValueChange = { zoomTemporary = it },
        //         onValueChangeFinished = {
        //             zoom = zoomTemporary
        //         }
        //     )
        //
        //     val zoomString = String.format(Locale.getDefault(), "%.2f", zoom)
        //     LabelValueListItem(
        //         LabelValueListModel(
        //             label = "Zoom",
        //             value = zoomString,
        //             clickAction = VglsAction.Noop,
        //         ),
        //         actionSink = { },
        //         modifier = Modifier,
        //         padding = PaddingValues()
        //     )
        // }
    }
}
