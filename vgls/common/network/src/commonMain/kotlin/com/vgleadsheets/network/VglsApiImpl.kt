package com.vgleadsheets.network

import com.vgleadsheets.network.model.ApiDigest
import com.vgleadsheets.network.model.ApiTime
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/** Ktor-backed [VglsApi]; [baseUrl] is the VGLS API root. */
class VglsApiImpl(
    private val client: HttpClient,
    private val baseUrl: String,
) : VglsApi {
    private val root = baseUrl.trimEnd('/')

    override suspend fun getDigest(): ApiDigest = client.get("$root/digest?v3=true").body()

    override suspend fun getLastUpdateTime(): ApiTime = client.get("$root/${VglsApi.LAST_UPDATE_PATH}").body()
}
