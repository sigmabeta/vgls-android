package com.vgleadsheets.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.http.HttpHeaders

/** Fake [SheetDownloadApi] (used in fake-API mode): a MockEngine client that returns an empty PDF. */
class FakeSheetDownloadApi : SheetDownloadApi {
    private val client = HttpClient(MockEngine) {
        engine {
            addHandler {
                respond(
                    content = ByteArray(0),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/pdf"),
                )
            }
        }
    }

    override suspend fun downloadFile(fileName: String, partApiId: String): HttpResponse = client.get("https://fake.local/$partApiId/$fileName")
}
