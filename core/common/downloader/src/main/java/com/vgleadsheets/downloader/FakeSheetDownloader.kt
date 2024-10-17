package com.vgleadsheets.downloader

import com.vgleadsheets.downloader.FileUtils.fileReference
import com.vgleadsheets.model.Part
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FakeSheetDownloader @Inject constructor(
    private val storageDirectoryProvider: StorageDirectoryProvider,
    private val urlInfoProvider: UrlInfoProvider,
    private val songRepository: SongRepository,
) : SheetDownloader {
    override suspend fun getSheet(config: PdfConfigById): SheetFileResult {
        val song = songRepository.getSong(config.songId).first()

        val fileName = song.filename
        val partApiId = urlInfoProvider.urlInfoFlow.value.partId ?: throw IllegalStateException("No part selected.")
        val actualPartApiId = if (partApiId == Part.VOCAL.apiId && song.lyricPageCount == 0) {
            Part.C.apiId
        } else {
            partApiId
        }

        val isAlternate = config.isAltSelected

        val targetFile = fileReference(
            storageDirectoryProvider.getStorageDirectory(),
            fileName,
            actualPartApiId,
            isAlternate
        )

        return SheetFileResult(
            targetFile,
            SheetSourceType.DISK
        )
    }
}
