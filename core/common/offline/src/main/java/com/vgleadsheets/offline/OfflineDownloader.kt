package com.vgleadsheets.offline

import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.repository.OfflineRepository
import kotlinx.coroutines.flow.first

class OfflineDownloader(
    private val offlineRepo: OfflineRepository,
    private val sheetDownloader: SheetDownloader,
    private val hatchet: Hatchet,
) {
    suspend fun checkAll() {
        var consecutiveFailures = 0
        consecutiveFailures = checkSongList("songs", offlineRepo.getAllSongs().first(), consecutiveFailures)
        consecutiveFailures = checkSongList("composer songs", offlineRepo.getAllComposerSongs().first(), consecutiveFailures)
        checkSongList("game songs", offlineRepo.getAllGameSongs().first(), consecutiveFailures)
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun checkSongList(label: String, songs: List<Song>, initialConsecutiveFailures: Int): Int {
        hatchet.i("Checking that all offline $label are downloaded.")
        var consecutiveFailures = initialConsecutiveFailures
        for (song in songs) {
            if (consecutiveFailures >= MAX_CONSECUTIVE_FAILURES) {
                hatchet.w("$MAX_CONSECUTIVE_FAILURES consecutive failures — aborting offline download.")
                return consecutiveFailures
            }
            try {
                hatchet.d("Checking song ${song.gameName} - ${song.name}")
                checkSong(song)
                consecutiveFailures = 0
            } catch (e: Exception) {
                hatchet.e("Failed to download ${song.gameName} - ${song.name}: ${e.message}")
                consecutiveFailures++
            }
        }
        return consecutiveFailures
    }

    companion object {
        private const val MAX_CONSECUTIVE_FAILURES = 3
    }

    private suspend fun checkSong(song: Song) {
        val hasVocals = song.hasVocals
        val hasAlts = song.altPageCount > 0

        val partsToDownload = if (hasVocals) {
            Part.entries
        } else {
            Part.entries.minus(Part.VOCAL)
        }

        val isAltValuesToDownload = if (hasAlts) {
            listOf(true, false)
        } else {
            listOf(false)
        }

        val variants = partsToDownload
            .flatMap { part ->
                isAltValuesToDownload.map { isAlt ->
                    part to isAlt
                }
            }

        variants.forEach { (part, isAlt) ->
            val exists = sheetDownloader.doesFileExist(
                fileName = song.filename,
                partApiId = part.apiId,
                isAlternate = isAlt,
            )

            if (exists) {
                hatchet.v("File already exists for ${song.gameName} - ${song.name} $part isAlt=$isAlt")
                return@forEach
            }

            hatchet.v("Requesting download of file for ${song.gameName} - ${song.name} $part isAlt=$isAlt")
            sheetDownloader.downloadFile(
                fileName = song.filename,
                partApiId = part.apiId,
                isAlternate = isAlt,
            )
        }
    }
}
