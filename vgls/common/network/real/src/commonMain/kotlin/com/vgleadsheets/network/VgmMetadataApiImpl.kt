package com.vgleadsheets.network

import com.vgleadsheets.network.model.ApiCatalogMapping
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

/** Ktor-backed [VgmMetadataApi]; [baseUrl] is the server root, e.g. `https://metadata.example.com/`. */
class VgmMetadataApiImpl(
    private val client: HttpClient,
    baseUrl: String,
) : VgmMetadataApi {
    private val root = baseUrl.trimEnd('/')

    override suspend fun getVglsCatalog(etag: String?): CatalogResult {
        // expectSuccess off: a 304 is an answer here, not a redirect to fail on.
        val response = client.get("$root/${VgmMetadataApi.VGLS_CATALOG_PATH}") {
            expectSuccess = false
            etag?.let { header(HttpHeaders.IfNoneMatch, it) }
        }
        return when {
            response.status == HttpStatusCode.NotModified -> CatalogResult.NotModified

            response.status.isSuccess() -> CatalogResult.Updated(
                mapping = response.body<ApiCatalogMapping>(),
                etag = response.headers[HttpHeaders.ETag],
            )

            else -> throw ResponseException(response, "vgm-metadata catalog request failed: ${response.status}")
        }
    }
}
