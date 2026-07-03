package com.vgleadsheets.downloader

import com.vgleadsheets.downloader.FileUtils.fileReference
import com.vgleadsheets.model.Part
import com.vgleadsheets.network.SheetDownloadApi
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import net.sigmabeta.sage.connectivity.HttpException
import net.sigmabeta.sage.connectivity.NetworkStatusProvider
import net.sigmabeta.sage.connectivity.NetworkUnavailableException
import io.ktor.client.statement.readRawBytes
import io.ktor.http.isSuccess
import okio.FileSystem
import okio.Path
import net.sigmabeta.sage.connectivity.allowsApiRequests
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.pdf.PdfConfigById
import dev.zacsweers.metro.Inject

class RealSheetDownloader @Inject constructor(
    private val storageDirectoryProvider: StorageDirectoryProvider,
    private val urlInfoProvider: UrlInfoProvider,
    private val songRepository: SongRepository,
    private val sheetDownloadApi: SheetDownloadApi,
    private val hatchet: Hatchet,
    private val networkStatusProvider: NetworkStatusProvider,
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

        if (FileSystem.SYSTEM.exists(targetFile)) {
            return SheetFileResult(
                targetFile,
                SheetSourceType.DISK
            )
        }
        val status = networkStatusProvider.status.value
        if (!status.allowsApiRequests) {
            throw NetworkUnavailableException(
                status,
                "Cannot download PDF for $config: VGLS network unavailable ($status)"
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
    ).let { FileSystem.SYSTEM.exists(it) }

    override suspend fun clearFilesForSong(fileName: String) {
        val songDir = storageDirectoryProvider.getStorageDirectory() / "pdfs" / fileName
        if (FileSystem.SYSTEM.exists(songDir)) {
            FileSystem.SYSTEM.deleteRecursively(songDir)
        }
    }

    @Suppress("MagicNumber")
    private suspend fun downloadSheet(
        fileName: String,
        partApiId: String,
        isAlternate: Boolean,
        targetFile: Path,
    ) {
        targetFile.parent?.let { FileSystem.SYSTEM.createDirectories(it) }

        val suffixedFileName = "$fileName${isAlternate.altSuffix()}.pdf"

        hatchet.d("Sending GET request for $suffixedFileName...")
        val response = sheetDownloadApi.downloadFile(suffixedFileName, partApiId)

        if (!response.status.isSuccess()) {
            throw HttpException(
                response.status.value,
                "Response \"${response.status.value} - ${response.status.description}\" received for filename $suffixedFileName"
            )
        }

        val bytes = response.readRawBytes()
        hatchet.d("Saving ${bytes.size / 1_024.0f} KiB to $targetFile...")
        FileSystem.SYSTEM.write(targetFile) { write(bytes) }
    }
}
