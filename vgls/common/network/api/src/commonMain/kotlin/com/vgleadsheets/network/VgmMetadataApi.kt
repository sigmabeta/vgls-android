package com.vgleadsheets.network

import com.vgleadsheets.network.model.ApiCatalogMapping

/**
 * The vgm-metadata server, which keeps every VGLS game matched to its IGDB game (cover art
 * included) and serves the whole mapping at once.
 */
interface VgmMetadataApi {
    /**
     * Fetches the VGLS catalog mapping. With the [etag] of a previous fetch, returns
     * [CatalogResult.NotModified] if the mapping hasn't changed since.
     */
    suspend fun getVglsCatalog(etag: String?): CatalogResult

    companion object {
        const val VGLS_CATALOG_PATH = "api/v1/catalogs/vgls"
    }
}

sealed interface CatalogResult {
    data object NotModified : CatalogResult

    /** A fresh [mapping], and the [etag] to send next time (null if the server sent none). */
    data class Updated(val mapping: ApiCatalogMapping, val etag: String?) : CatalogResult
}

/** Stands in when no vgm-metadata server is configured: there's never anything new. */
object DisabledVgmMetadataApi : VgmMetadataApi {
    override suspend fun getVglsCatalog(etag: String?): CatalogResult = CatalogResult.NotModified
}
