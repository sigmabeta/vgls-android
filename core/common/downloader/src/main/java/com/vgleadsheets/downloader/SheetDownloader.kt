package com.vgleadsheets.downloader

import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.network.SheetDownloadApi
import com.vgleadsheets.repository.VglsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.IOException
import javax.inject.Inject

class SheetDownloader @Inject constructor(
    private val storageDirectoryProvider: StorageDirectoryProvider,
    private val repository: VglsRepository,
    private val sheetDownloadApi: SheetDownloadApi,
    private val hatchet: Hatchet,
) {
    suspend fun getSheet(id: Long, partApiId: String) = repository
        .getSong(id)
        .map { getSheetInternal(it.filename, partApiId) }
        .first()

    private suspend fun getSheetInternal(
        fileName: String,
        partApiId: String
    ): SheetFileResult {
        val targetFile = fileReference(fileName, partApiId)

        if (targetFile.exists()) {
            return SheetFileResult(
                targetFile,
                SheetSourceType.DISK
            )
        }

        downloadSheet(fileName, partApiId, targetFile)

        return SheetFileResult(
            targetFile,
            SheetSourceType.NETWORK
        )
    }

    @Suppress("MagicNumber")
    private suspend fun downloadSheet(
        fileName: String,
        partApiId: String,
        targetFile: File,
    ) {
        val songDirectory = targetFile.parentFile
        val gameDirectory = songDirectory.parentFile
        val pdfsDirectory = gameDirectory.parentFile

        pdfsDirectory.ensureExists()
        gameDirectory.ensureExists()
        songDirectory.ensureExists()

        val suffixedFileName = "$fileName.pdf"

        hatchet.d("Sending GET request for $suffixedFileName...")
        val response = sheetDownloadApi.downloadFile(suffixedFileName, partApiId)

        if (!response.isSuccessful) {
            throw IOException(
                "Response \"${response.code()} - ${response.message()}\" received for filename $suffixedFileName"
            )
        }

        val body = response.body() ?: throw IOException("Somehow received empty response? Nani!?!?")

        val bytes = body.bytes()
        hatchet.d("Saving ${bytes.size / 1_024.0f} KB to ${targetFile.absolutePath}...")
        targetFile.writeBytes(bytes)
    }

    private fun fileReference(
        fileName: String,
        partApiId: String
    ) = File(
        storageDirectoryProvider.getStorageDirectory(),
        "pdfs/$fileName/$partApiId.pdf"
    )

    private fun File.ensureExists() {
        if (exists()) {
            return
        }

        hatchet.v("Creating directory: $absolutePath")
        mkdir()
    }
}
