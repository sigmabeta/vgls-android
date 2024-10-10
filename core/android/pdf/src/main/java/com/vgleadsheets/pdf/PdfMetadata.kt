package com.vgleadsheets.pdf

import coil.annotation.ExperimentalCoilApi
import coil.decode.ImageSource

@OptIn(ExperimentalCoilApi::class)
data class PdfMetadata(
    val pageNumber: Int
) : ImageSource.Metadata()
