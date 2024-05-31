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
        .map { getSheetInternal(it.name, it.gameName, partApiId) }
        .first()

    suspend fun getSheet(
        title: String,
        gameName: String,
        partApiId: String
    ) = getSheetInternal(
        title,
        gameName,
        partApiId
    )

    private suspend fun getSheetInternal(
        title: String,
        gameName: String,
        partApiId: String
    ): File {
        val targetFile = fileReference(title, gameName, partApiId)

        if (targetFile.exists()) {
            return targetFile
        }

        downloadSheet(title, gameName, partApiId, targetFile)

        return targetFile
    }

    private suspend fun downloadSheet(
        title: String,
        gameName: String,
        partApiId: String,
        targetFile: File,
    ) {
        val songDirectory = targetFile.parentFile
        val gameDirectory = songDirectory.parentFile

        gameDirectory.ensureExists()
        songDirectory.ensureExists()

        val remoteFileName = remoteFileName(title, gameName)

        hatchet.d("Sending GET request for $remoteFileName...")
        val response = sheetDownloadApi.downloadFile(remoteFileName, partApiId)

        if (!response.isSuccessful) {
            throw IOException("Response \"${response.code()} - ${response.message()}\" received for filename $remoteFileName")
        }

        val body = response.body() ?: throw IOException("Somehow received empty response? Nani!?!?")

        val bytes = body.bytes()
        hatchet.d("Saving ${bytes.size / 1_024.0f} KB to ${targetFile.absolutePath}...")
        targetFile.writeBytes(bytes)
    }

    private fun remoteFileName(title: String, gameName: String) = "$gameName - $title.pdf"

    private fun fileReference(
        title: String,
        gameName: String,
        partApiId: String
    ) = File(
        storageDirectoryProvider.getStorageDirectory(),
        "$gameName/$title/$partApiId.pdf"
    )

    private fun File.ensureExists() {
        if (exists()) {
            return
        }

        hatchet.v("Creating directory: $absolutePath")
        mkdir()
    }
}

