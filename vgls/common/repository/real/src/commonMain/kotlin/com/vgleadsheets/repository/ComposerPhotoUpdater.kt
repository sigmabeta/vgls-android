package com.vgleadsheets.repository

import com.vgleadsheets.database.TransactionRunner
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.model.ComposerPhoto
import com.vgleadsheets.network.CatalogResult
import com.vgleadsheets.network.VgmMetadataApi
import com.vgleadsheets.network.model.ApiComposerCatalogMapping
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.storage.common.Storage

/**
 * Pulls the vgm-metadata server's VGLS composer → Wikidata mapping and stores each composer's photo
 * and its credit. Photos are a nicety, so failures are logged and never surfaced to the user.
 */
class ComposerPhotoUpdater(
    private val api: VgmMetadataApi,
    private val composerDataSource: ComposerDataSource,
    private val transactionRunner: TransactionRunner,
    private val storage: Storage,
    private val hatchet: Hatchet,
) {
    private val mutex = Mutex()

    /**
     * Fetches the mapping and applies it if it changed since the last pull. [force] ignores the last
     * pull: use it after the composer table was rewritten, when new or re-created rows have no photo
     * even though the mapping itself is unchanged.
     */
    @Suppress("TooGenericExceptionCaught")
    suspend fun refresh(force: Boolean = false) = mutex.withLock {
        try {
            // Trust the ETag only while we actually have photos: if the table was cleared (or the
            // first pull never landed) nothing has a photo, so re-fetch even when the mapping is
            // unchanged — otherwise a reset would strand the app with no photos until the next change.
            val mustFetch = force || !composerDataSource.hasMetadataPhoto()
            val etag = if (mustFetch) null else storage.savedStringFlow(KEY_ETAG).first()?.ifEmpty { null }
            when (val result = api.getVglsComposersCatalog(etag)) {
                CatalogResult.NotModified -> hatchet.v("Composer photos unchanged.")

                is CatalogResult.Updated -> {
                    val photos = result.mapping.toPhotos()
                    transactionRunner.inTransaction { composerDataSource.replaceMetadataPhotos(photos) }
                    storage.saveString(KEY_ETAG, result.etag.orEmpty())
                    hatchet.i("Applied photos for ${photos.size} composers.")
                }
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (ex: Exception) {
            hatchet.w("Composer photo pull failed: ${ex.message}")
        }
    }

    /** VGLS composer id → photo, for the matched composers that have one. */
    private fun ApiComposerCatalogMapping.toPhotos(): Map<Long, ComposerPhoto> =
        entries.mapNotNull { (composerId, entry) ->
            val id = composerId.toLongOrNull()?.takeUnless { it in SKIPPED_COMPOSER_IDS } ?: return@mapNotNull null
            val url = entry.match.imageUrl ?: return@mapNotNull null
            id to ComposerPhoto(
                url = url,
                author = entry.match.imageAuthor,
                license = entry.match.imageLicense,
                licenseUrl = entry.match.imageLicenseUrl,
                sourceUrl = entry.match.imageSourceUrl,
            )
        }.toMap()

    companion object {
        private const val KEY_ETAG = "composer.photos.etag"

        /**
         * VGLS composers that aren't people, so any Wikidata match is wrong: 239 "K.K. Slider". Keep
         * the collage for them.
         */
        val SKIPPED_COMPOSER_IDS = setOf(239L)
    }
}
