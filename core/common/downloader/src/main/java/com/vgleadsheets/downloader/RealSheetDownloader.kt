package com.vgleadsheets.downloader

import com.vgleadsheets.downloader.FileUtils.fileReference
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Part
import com.vgleadsheets.network.SheetDownloadApi
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.flow.catch
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
        val song = songRepository
            .getSong(config.songId)
            .catch { throw IllegalArgumentException("No song found with id ${config.songId}.") }
            .first()

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
        hatchet.v("Download request for $config")
        downloadSheet(fileName, actualPartApiId, isAlternate, targetFile)

        return SheetFileResult(
            targetFile,
            SheetSourceType.NETWORK
        )
    }

    override suspend fun downloadFile(
        fileName: String,
        partApiId: String,
        isAlternate: Boolean,
    ) {
        val targetFile = fileReference(
            storageDirectoryProvider.getStorageDirectory(),
            fileName,
            partApiId,
            isAlternate
        )
        downloadSheet(fileName, partApiId, isAlternate, targetFile)
    }

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

        pdfsDirectory.ensureDirectoryExists()
        gameDirectory.ensureDirectoryExists()
        songDirectory.ensureDirectoryExists()

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
