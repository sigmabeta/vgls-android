package com.vgleadsheets.repository

import com.vgleadsheets.database.TransactionRunner
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.model.IgdbImages
import com.vgleadsheets.network.CatalogResult
import com.vgleadsheets.network.VgmMetadataApi
import com.vgleadsheets.network.model.ApiCatalogMapping
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.storage.common.Storage

/**
 * Pulls the vgm-metadata server's VGLS → IGDB mapping and stores each game's IGDB cover image id.
 * Cover art is a nicety, so failures are logged and never surfaced to the user.
 */
class IgdbCoverUpdater(
    private val api: VgmMetadataApi,
    private val gameDataSource: GameDataSource,
    private val transactionRunner: TransactionRunner,
    private val storage: Storage,
    private val hatchet: Hatchet,
) {
    private val mutex = Mutex()

    /**
     * Fetches the mapping and applies it if it changed since the last pull. [force] ignores the last
     * pull: use it after the game table was rewritten, when new or re-created rows have no cover
     * even though the mapping itself is unchanged.
     */
    @Suppress("TooGenericExceptionCaught")
    suspend fun refresh(force: Boolean = false) = mutex.withLock {
        try {
            val etag = if (force) null else storage.savedStringFlow(KEY_ETAG).first()?.ifEmpty { null }
            when (val result = api.getVglsCatalog(etag)) {
                CatalogResult.NotModified -> hatchet.v("IGDB mapping unchanged.")

                is CatalogResult.Updated -> {
                    val imageIds = result.mapping.toImageIds()
                    transactionRunner.inTransaction { gameDataSource.replaceIgdbImageIds(imageIds) }
                    storage.saveString(KEY_ETAG, result.etag.orEmpty())
                    hatchet.i("Applied IGDB covers for ${imageIds.size} games.")
                }
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (ex: Exception) {
            hatchet.w("IGDB mapping pull failed: ${ex.message}")
        }
    }

    /** VGLS game id → IGDB cover image id, for the matched games that have a cover. */
    private fun ApiCatalogMapping.toImageIds(): Map<Long, String> = entries.mapNotNull { (gameId, entry) ->
        val id = gameId.toLongOrNull()?.takeUnless { it in SKIPPED_GAME_IDS }
        val imageId = entry.match.imageUrl?.let(IgdbImages::imageIdFromUrl)
        if (id != null && imageId != null) id to imageId else null
    }.toMap()

    companion object {
        private const val KEY_ETAG = "igdb.mapping.etag"

        /**
         * VGLS games that aren't games (system and menu music), so any IGDB match is wrong: 231 "Wii",
         * 368 "Nintendo DSi System Music", 13 "BS-X (Satellaview) BIOS".
         */
        val SKIPPED_GAME_IDS = setOf(231L, 368L, 13L)
    }
}
