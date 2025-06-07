package com.vgleadsheets.downloader

import com.vgleadsheets.pdf.PdfConfigById

interface SheetDownloader {
    suspend fun getSheet(config: PdfConfigById): SheetFileResult
}
