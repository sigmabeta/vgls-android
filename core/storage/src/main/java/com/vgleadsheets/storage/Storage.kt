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
    fun getAllDebugSettings(): Single<List<Setting>>
    fun getDebugSettingNetworkEndpoint(): Single<DropdownSetting>
    fun getDebugSettingShowPerfView(): Single<BooleanSetting>

    // Saving Debug Settings
    fun saveDebugSelectedNetworkEndpoint(newValue: Int): Single<String>
    fun saveDebugSettingPerfView(newValue: Boolean): Single<String>

    companion object {
        const val KEY_SELECTED_TOP_LEVEL = "KEY_SELECTED_TOP_LEVEL"
        const val KEY_SELECTED_PART = "KEY_SELECTED_PART"

        const val KEY_SHEETS_KEEP_SCREEN_ON = "SETTING_SHEET_KEEP_SCREEN_ON"

        const val KEY_DEBUG_NETWORK_ENDPOINT = "DEBUG_NETWORK_ENDPOINT"
        const val KEY_DEBUG_MISC_PERF_VIEW = "DEBUG_MISC_PERF_VIEW"
    }
}
