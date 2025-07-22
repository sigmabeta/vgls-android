package com.vgleadsheets.offline

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.downloader.SheetDownloader
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.repository.OfflineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OfflineDownloader(
    private val offlineRepo: OfflineRepository,
    private val sheetDownloader: SheetDownloader,
    private val hatchet: Hatchet,
    private val scope: CoroutineScope,
    private val dispatchers: VglsDispatchers,
) {
    fun checkAll() {
        checkAllSongs()
    }

    private fun checkAllSongs() {
        hatchet.i("Checking that all offline songs are downloaded.")
        scope.launch(dispatchers.disk) {
            offlineRepo.getAllSongs()
                .filter { it.isNotEmpty() }
                .first()
                .onEach {
                    hatchet.d("Checking song ${it.gameName} - ${it.name}")
                    checkSong(it)
                }
        }
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
