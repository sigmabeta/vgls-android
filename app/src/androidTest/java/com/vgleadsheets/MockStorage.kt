package com.vgleadsheets

import com.vgleadsheets.storage.BooleanSetting
import com.vgleadsheets.storage.DropdownSetting
import com.vgleadsheets.storage.SimpleStorage
import com.vgleadsheets.storage.Storage
import io.reactivex.Single

class MockStorage : Storage {
    var savedTopLevelScreen: String = ""

    var savedSelectedPart: String = ""

    var sheetScreenOn = false

    override fun getSavedTopLevelScreen() = Single.just(savedTopLevelScreen)

    override fun getSavedSelectedPart() = Single.just(savedSelectedPart)

    override fun saveTopLevelScreen(screenId: String) =
        Single.error<String>(NotImplementedError("Implement this!"))

    override fun saveSelectedPart(partId: String) =
        Single.error<String>(NotImplementedError("Implement this!"))

    override fun getAllSettings() = Single
        .concat(
            // TODO Once there's actually more than one of these, we don't need the listOf call
            listOf(
                getSettingSheetScreenOn()
            )
        )
        .toList()

    override fun getSettingSheetScreenOn() = Single.just(
        BooleanSetting(
            SimpleStorage.KEY_SHEETS_KEEP_SCREEN_ON,
            R.string.label_setting_screen_on,
            sheetScreenOn
        )
    )

    override fun saveSettingSheetScreenOn(setting: Boolean) = Single.create<String> {
        sheetScreenOn = setting
        it.onSuccess(setting.toString())
    }

    override fun getAllDebugSettings() = Single
        .concat(
            getDebugSettingNetworkEndpoint(),
            getDebugSettingShowPerfView()
        )
        .toList()

    override fun getDebugSettingNetworkEndpoint() = Single.just(
        DropdownSetting(
            SimpleStorage.KEY_DEBUG_NETWORK_ENDPOINT,
            R.string.label_debug_network_endpoint,
            NetworkEndpoint.MOCK.ordinal,
            NetworkEndpoint.values().map { it.displayStringId }
        )
    )

    override fun getDebugSettingShowPerfView() = Single.just(
        BooleanSetting(
            SimpleStorage.KEY_DEBUG_MISC_PERF_VIEW,
            R.string.label_debug_misc_perf_view,
            false
        )
    )

    override fun saveDebugSelectedNetworkEndpoint(newValue: Int) =
        Single.error<String>(NotImplementedError("Implement this!"))

    override fun saveDebugSettingPerfView(newValue: Boolean) =
        Single.error<String>(NotImplementedError("Implement this!"))
}
