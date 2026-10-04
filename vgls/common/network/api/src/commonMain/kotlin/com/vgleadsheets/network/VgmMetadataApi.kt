package com.vgleadsheets.network

import com.vgleadsheets.network.model.ApiCatalogMapping
import com.vgleadsheets.network.model.ApiComposerCatalogMapping

/**
 * The vgm-metadata server. It keeps every VGLS game matched to its IGDB game (cover art included)
 * and every composer matched to a Wikidata person (photo included), and serves each whole mapping
 * at once.
 */
interface VgmMetadataApi {
    /**
     * Fetches the VGLS game → IGDB catalog mapping. With the [etag] of a previous fetch, returns
     * [CatalogResult.NotModified] if the mapping hasn't changed since.
     */
    suspend fun getVglsCatalog(etag: String?): CatalogResult<ApiCatalogMapping>

    /**
     * Fetches the VGLS composer → Wikidata catalog mapping (photos and their credit). With the
     * [etag] of a previous fetch, returns [CatalogResult.NotModified] if it hasn't changed since.
     */
    suspend fun getVglsComposersCatalog(etag: String?): CatalogResult<ApiComposerCatalogMapping>

    companion object {
        const val VGLS_CATALOG_PATH = "api/v1/catalogs/vgls"
        const val VGLS_COMPOSERS_CATALOG_PATH = "api/v1/catalogs/vgls-composers"
    }
}

sealed interface CatalogResult<out T> {
    data object NotModified : CatalogResult<Nothing>

    /** A fresh [mapping], and the [etag] to send next time (null if the server sent none). */
    data class Updated<T>(val mapping: T, val etag: String?) : CatalogResult<T>
}

/** Stands in when no vgm-metadata server is configured: there's never anything new. */
object DisabledVgmMetadataApi : VgmMetadataApi {
    override suspend fun getVglsCatalog(etag: String?): CatalogResult<ApiCatalogMapping> = CatalogResult.NotModified

    override suspend fun getVglsComposersCatalog(etag: String?): CatalogResult<ApiComposerCatalogMapping> =
        CatalogResult.NotModified
}
