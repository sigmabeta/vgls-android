package com.vgleadsheets.network

import com.vgleadsheets.model.giantbomb.GiantBombGame
import com.vgleadsheets.model.giantbomb.GiantBombImage
import com.vgleadsheets.model.giantbomb.GiantBombPerson
import com.vgleadsheets.model.giantbomb.GiantBombSearchResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.Random

class MockGiantBombApi(
    private val random: Random
) : GiantBombApi {
    override fun searchForGame(
        name: String,
        resources: String,
        field_list: String
    ) = Single
        .just(
            GiantBombSearchResponse(
                "",
                listOf(
                    GiantBombGame(
                        random.nextLong(),
                        name,
                        null,
                        GiantBombImage(
                            "gooseBomb"
                        )
                    )
                )
            )
        )
        .subscribeOn(Schedulers.io())

    override fun searchForComposer(
        name: String,
        resources: String,
        field_list: String
    ) = Single
        .just(
            GiantBombSearchResponse(
                "",
                listOf(
                    GiantBombPerson(
                        random.nextLong(),
                        name,
                        null,
                        GiantBombImage(
                            "gooseBomb"
                        )
                    )
                )
            )
        )
        .subscribeOn(Schedulers.io())
}
