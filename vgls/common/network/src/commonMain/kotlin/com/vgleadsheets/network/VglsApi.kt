package com.vgleadsheets.network

import com.vgleadsheets.network.model.ApiDigest
import com.vgleadsheets.network.model.ApiTime

interface VglsApi {
    suspend fun getDigest(): ApiDigest

    suspend fun getLastUpdateTime(): ApiTime

    companion object {
        const val LAST_UPDATE_PATH = "digest/last-updated?rfc3339=true"
    }
}
