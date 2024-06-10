package com.vgleadsheets.storage

@Suppress("TooManyFunctions")
interface Storage {
    // Loading Defaults
    suspend fun getSavedTopLevelScreen(): String
    suspend fun getSavedSelectedPart(): String

    // Saving Defaults
    fun saveTopLevelScreen(screenId: String)
    fun saveSelectedPart(partId: String)

    // Loading Settings
    suspend fun getAllSettings(): List<BooleanSetting>
    suspend fun getSettingSheetScreenOn(): BooleanSetting

    // Saving Settings
    fun saveSettingSheetScreenOn(setting: Boolean)

    // Loading Debug Settings
    suspend fun getAllDebugSettings(): List<Setting>
    suspend fun getDebugSettingNetworkEndpoint(): DropdownSetting
    suspend fun getDebugSettingShowPerfView(): BooleanSetting

    // Saving Debug Settings
    fun saveDebugSelectedNetworkEndpoint(newValue: Int)
    fun saveDebugSettingPerfView(newValue: Boolean)

    companion object {
        const val KEY_SELECTED_TOP_LEVEL = "KEY_SELECTED_TOP_LEVEL"
        const val KEY_SELECTED_PART = "KEY_SELECTED_PART"

        const val KEY_SHEETS_KEEP_SCREEN_ON = "SETTING_SHEET_KEEP_SCREEN_ON"

        const val KEY_DEBUG_NETWORK_ENDPOINT = "DEBUG_NETWORK_ENDPOINT"
        const val KEY_DEBUG_MISC_PERF_VIEW = "DEBUG_MISC_PERF_VIEW"
    }
}
