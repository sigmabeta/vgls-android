package com.vgleadsheets.storage

import io.reactivex.Single

interface Storage {
    // Defaults
    fun getSavedTopLevelScreen(): Single<String>
    fun getSavedSelectedPart(): Single<String>

    fun saveTopLevelScreen(screenId: String): Single<String>
    fun saveSelectedPart(partId: String): Single<String>

    // Settings
    fun getAllSettings(): Single<List<Setting>>
    fun getSettingSheetScreenOn(): Single<Setting>
    fun saveSettingSheetScreenOn(setting: Boolean): Single<String>

    // Debug Settings
    fun getAllDebugSettings(): Single<List<Setting>>
}
