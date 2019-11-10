package com.vgleadsheets.network

import com.vgleadsheets.model.giantbomb.GiantBombGame
import com.vgleadsheets.model.giantbomb.GiantBombImage
import com.vgleadsheets.model.giantbomb.GiantBombPerson
import com.vgleadsheets.model.giantbomb.GiantBombSearchResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class GiantBombNoKeyApi : GiantBombApi {
    override fun searchForGame(
        name: String,
        resources: String,
        field_list: String
    ) = Single.just(
        GiantBombSearchResponse(
            "",
            listOf(
                GiantBombGame(
                    GiantBombGame.ID_NO_API,
                    name,
                    null,
                    GiantBombImage(
                        ""
                    )
                )
            )
        )
    ).subscribeOn(Schedulers.io())

    override fun searchForComposer(
        name: String,
        resources: String,
        field_list: String
    ) = Single.just(
        GiantBombSearchResponse(
            "",
            listOf(
                GiantBombPerson(
                    GiantBombPerson.ID_NO_API,
                    name,
                    null,
                    GiantBombImage(
                        ""
                    )
                )
            )
        )
    ).subscribeOn(Schedulers.io())
}
