package com.vgleadsheets.network

import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.model.jam.ApiJam
import com.vgleadsheets.model.jam.ApiSetlist
import com.vgleadsheets.model.time.ApiTime
import io.reactivex.Observable
import io.reactivex.Single

class MockVglsApi : VglsApi {
    override fun getDigest(): Single<List<VglsApiGame>> = Single.error(NotImplementedError())

    override fun getLastUpdateTime(): Single<ApiTime> = Single.error(NotImplementedError())

    override fun getJamState(name: String): Observable<ApiJam> = Observable.error(NotImplementedError())

    override fun getSetlistForJam(name: String): Single<ApiSetlist> = Single.error(NotImplementedError())
}
