package com.vgleadsheets.downloader

import java.io.File

data class SheetFileResult(
    val file: File,
    val sourceType: SheetSourceType
)
