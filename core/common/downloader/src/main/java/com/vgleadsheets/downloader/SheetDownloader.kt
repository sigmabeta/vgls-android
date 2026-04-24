package com.vgleadsheets.downloader

import com.vgleadsheets.pdf.PdfConfigById

interface SheetDownloader {
    suspend fun getSheet(config: PdfConfigById): SheetFileResult

    suspend fun downloadFile(
        fileName: String,
        partApiId: String,
        isAlternate: Boolean,
    )

    suspend fun doesFileExist(
        fileName: String,
        partApiId: String,
        isAlternate: Boolean,
    ): Boolean

    suspend fun clearFilesForSong(fileName: String)
}
