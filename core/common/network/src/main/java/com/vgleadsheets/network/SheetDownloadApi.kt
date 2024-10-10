package com.vgleadsheets.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SheetDownloadApi {
    @GET("{partApiId}/{fileName}")
    suspend fun downloadFile(
        /*Octo*/
        @Path("fileName") fileName: String,
        /*Octo*/
        @Path("partApiId") partApiId: String,
    ): Response<ResponseBody>
}
