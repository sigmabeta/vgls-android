package com.vgleadsheets.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Response

class FakeSheetDownloadApi : SheetDownloadApi {
    override suspend fun downloadFile(
        fileName: String,
        partApiId: String,
    ): Response<ResponseBody> {
        return Response.success(
            ResponseBody.create(
                "application/pdf".toMediaType(),
                ""
            )
        )
    }
}
