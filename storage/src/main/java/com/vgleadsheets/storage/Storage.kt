package com.vgleadsheets.storage

import io.reactivex.Single

@Suppress("TooManyFunctions")
interface Storage {
    // Loading Defaults
    fun getSavedTopLevelScreen(): Single<String>
    fun getSavedSelectedPart(): Single<String>

    // Saving Defaults
    fun saveTopLevelScreen(screenId: String): Single<String>
    fun saveSelectedPart(partId: String): Single<String>

    // Loading Settings
    fun getAllSettings(): Single<List<BooleanSetting>>
    fun getSettingSheetScreenOn(): Single<BooleanSetting>

    // Saving Settings
    fun saveSettingSheetScreenOn(setting: Boolean): Single<String>

    // Loading Debug Settings
    fun getAllDebugSettings(): Single<List<DropdownSetting>>
    fun getDebugSettingNetworkEndpoint(): Single<DropdownSetting>

    // Saving Debug Settings
    fun saveSelectedNetworkEndpoint(newValue: Int): Single<String>
}
