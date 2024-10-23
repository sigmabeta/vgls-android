package com.vgleadsheets.pdf

import coil3.annotation.ExperimentalCoilApi
import coil3.decode.ImageSource

@OptIn(ExperimentalCoilApi::class)
data class PdfMetadata(
    val pageNumber: Int
) : ImageSource.Metadata()
