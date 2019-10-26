package com.vgleadsheets.storage

import io.reactivex.Single

interface Storage {
    fun getSavedTopLevelScreen(): Single<String>
    fun getSavedSelectedPart(): Single<String>

    fun saveTopLevelScreen(screenId: String): Single<String>
    fun saveSelectedPart(partId: String): Single<String>
}
