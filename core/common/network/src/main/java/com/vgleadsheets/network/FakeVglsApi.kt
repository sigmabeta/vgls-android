package com.vgleadsheets.network

import com.vgleadsheets.network.model.ApiDigest
import com.vgleadsheets.network.model.ApiTime

class FakeVglsApi(
    private val fakeModelGenerator: FakeModelGenerator
) : VglsApi {
    override suspend fun getDigest() = generateDigest()

    // TODO Fill this in
    override suspend fun getLastUpdateTime() = ApiTime("2017-04-01T23:30:06Z")

    private fun generateDigest(): ApiDigest {
        val games = fakeModelGenerator.possibleGames!!
        val composers = games
            .map { it.songs }
            .flatten()
            .map { it.composers }
            .flatten()

        return ApiDigest(
            composers,
            games
        )
    }
}
