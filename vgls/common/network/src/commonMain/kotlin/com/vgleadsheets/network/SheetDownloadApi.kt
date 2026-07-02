package com.vgleadsheets.network

import io.ktor.client.statement.HttpResponse

interface SheetDownloadApi {
    suspend fun downloadFile(
        fileName: String,
        partApiId: String,
    ): HttpResponse
}
