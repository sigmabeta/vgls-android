package com.vgleadsheets.downloader

import com.vgleadsheets.downloader.FileUtils.fileReference
import com.vgleadsheets.model.Part
import net.sigmabeta.sage.pdf.PdfConfigById
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.flow.first
import java.io.File
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

    override suspend fun downloadFile(
        fileName: String,
        partApiId: String,
        isAlternate: Boolean,
    ) = Unit

    override suspend fun doesFileExist(
        fileName: String,
        partApiId: String,
        isAlternate: Boolean,
    ) = fileReference(
        storageDirectoryProvider.getStorageDirectory(),
        fileName,
        partApiId,
        isAlternate
    ).exists()

    override suspend fun clearFilesForSong(fileName: String) {
        File(storageDirectoryProvider.getStorageDirectory(), "pdfs/$fileName")
            .deleteRecursively()
    }
}
