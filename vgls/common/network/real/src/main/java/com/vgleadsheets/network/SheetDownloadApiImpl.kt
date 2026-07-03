package com.vgleadsheets.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

/** Ktor-backed [SheetDownloadApi]; [baseUrl] is the PDF root. Returns the raw response so the
 *  caller can check status and stream the bytes. */
class SheetDownloadApiImpl(
    private val client: HttpClient,
    private val baseUrl: String,
) : SheetDownloadApi {
    private val root = baseUrl.trimEnd('/')

    override suspend fun downloadFile(fileName: String, partApiId: String): HttpResponse = client.get("$root/$partApiId/$fileName")
}
