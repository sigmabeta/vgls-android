package com.vgleadsheets.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.request.ImageRequest
import me.saket.telephoto.zoomable.ZoomSpec
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import me.saket.telephoto.zoomable.rememberZoomableImageState
import me.saket.telephoto.zoomable.rememberZoomableState

@Composable
fun ZoomableImageTest(modifier: Modifier = Modifier) {
    val model = with(ImageRequest.Builder(LocalContext.current)) {
        data("https://randomfox.ca/images/64.jpg")
        build()
    }

    println("Rendering zoomable image: $model")


    val zoomableState = rememberZoomableState(
        zoomSpec = ZoomSpec(maxZoomFactor = 4f)
    )

    ZoomableAsyncImage(
        state = rememberZoomableImageState(zoomableState),
        model = model,
        contentScale = ContentScale.Crop,
        alignment = Alignment.TopCenter,
        contentDescription = null,
        modifier = modifier.fillMaxSize()
    )
}
