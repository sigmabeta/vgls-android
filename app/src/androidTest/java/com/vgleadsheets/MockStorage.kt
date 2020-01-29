package com.vgleadsheets

import com.vgleadsheets.common.debug.GiantBombNetworkEndpoint
import com.vgleadsheets.common.debug.NetworkEndpoint
import com.vgleadsheets.storage.BooleanSetting
import com.vgleadsheets.storage.DropdownSetting
import com.vgleadsheets.storage.R
import com.vgleadsheets.storage.SimpleStorage
import com.vgleadsheets.storage.Storage
import io.reactivex.Single

@Suppress("TooManyFunctions")
class MockStorage : Storage {
    var savedTopLevelScreen: String = ""

    var savedSelectedPart: String = ""

    override fun getSavedTopLevelScreen() = Single.just(savedTopLevelScreen)

    override fun getSavedSelectedPart() = Single.just(savedSelectedPart)

    override fun saveTopLevelScreen(screenId: String) =
        Single.error<String>(NotImplementedError("Implement this!"))

    override fun saveSelectedPart(partId: String) =
        Single.error<String>(NotImplementedError("Implement this!"))

    override fun getAllSettings() =
        Single.error<List<BooleanSetting>>(NotImplementedError("Implement this!"))

    override fun getSettingSheetScreenOn() =
        Single.error<BooleanSetting>(NotImplementedError("Implement this!"))

    override fun saveSettingSheetScreenOn(setting: Boolean) =
        Single.error<String>(NotImplementedError("Implement this!"))

    override fun getAllDebugSettings() =
        Single.error<List<DropdownSetting>>(NotImplementedError("Implement this!"))

    override fun getDebugSettingNetworkEndpoint() = Single.just(
        DropdownSetting(
            SimpleStorage.KEY_DEBUG_NETWORK_ENDPOINT,
            R.string.label_debug_network_endpoint,
            NetworkEndpoint.MOCK.ordinal,
            NetworkEndpoint.values().map { it.displayStringId }
        )
    )

    override fun getDebugSettingNetworkGiantBombEndpoint() = Single.just(
        DropdownSetting(
            SimpleStorage.KEY_DEBUG_NETWORK_GB_ENDPOINT,
            R.string.label_debug_network_giant_bomb_endpoint,
            GiantBombNetworkEndpoint.MOCK.ordinal,
            GiantBombNetworkEndpoint.values().map { it.displayStringId }
        )
    )

    override fun saveSelectedNetworkEndpoint(newValue: Int) =
        Single.error<String>(NotImplementedError("Implement this!"))

    override fun saveSelectedNetworkGiantBombEndpoint(newValue: Int) =
        Single.error<String>(NotImplementedError("Implement this!"))
}
