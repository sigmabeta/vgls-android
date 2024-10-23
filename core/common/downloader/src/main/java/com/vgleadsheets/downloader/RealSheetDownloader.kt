package com.vgleadsheets.downloader

import com.vgleadsheets.downloader.FileUtils.fileReference
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Part
import com.vgleadsheets.network.SheetDownloadApi
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.IOException
import javax.inject.Inject

class RealSheetDownloader @Inject constructor(
    private val storageDirectoryProvider: StorageDirectoryProvider,
    private val urlInfoProvider: UrlInfoProvider,
    private val songRepository: SongRepository,
    private val sheetDownloadApi: SheetDownloadApi,
    private val hatchet: Hatchet,
) : SheetDownloader {
    override suspend fun getSheet(config: PdfConfigById): SheetFileResult {
        hatchet.v("Download request for $config")
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

        if (targetFile.exists()) {
            return SheetFileResult(
                targetFile,
                SheetSourceType.DISK
            )
        }

        downloadSheet(fileName, actualPartApiId, isAlternate, targetFile)

        return SheetFileResult(
            targetFile,
            SheetSourceType.NETWORK
        )
    }

    @Suppress("MagicNumber")
    private suspend fun downloadSheet(
        fileName: String,
        partApiId: String,
        isAlternate: Boolean,
        targetFile: File,
    ) {
        val songDirectory = targetFile.parentFile
        val gameDirectory = songDirectory.parentFile
        val pdfsDirectory = gameDirectory.parentFile

        pdfsDirectory.ensureExists()
        gameDirectory.ensureExists()
        songDirectory.ensureExists()

        val suffixedFileName = "$fileName${isAlternate.altSuffix()}.pdf"

        hatchet.d("Sending GET request for $suffixedFileName...")
        val response = sheetDownloadApi.downloadFile(suffixedFileName, partApiId)

        if (!response.isSuccessful) {
            throw IOException(
                "Response \"${response.code()} - ${response.message()}\" received for filename $suffixedFileName"
            )
        }

        val body = response.body() ?: throw IOException("Somehow received empty response? Nani!?!?")

        val bytes = body.bytes()
        hatchet.d("Saving ${bytes.size / 1_024.0f} KiB to ${targetFile.absolutePath}...")
        targetFile.writeBytes(bytes)
    }
}
