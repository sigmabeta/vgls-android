package com.vgleadsheets.storage

import com.uber.simplestore.SimpleStore
import com.vgleadsheets.common.debug.NetworkEndpoint
import io.reactivex.Single

internal class SimpleStorage(val simpleStore: SimpleStore) : Storage {
    override fun getSavedTopLevelScreen() =
        Single.fromFuture(simpleStore.getString(KEY_SELECTED_TOP_LEVEL))

    override fun getSavedSelectedPart() =
        Single.fromFuture(simpleStore.getString(KEY_SELECTED_PART))

    override fun saveTopLevelScreen(screenId: String) = Single.fromFuture(
        simpleStore.putString(KEY_SELECTED_TOP_LEVEL, screenId)
    )

    override fun saveSelectedPart(partId: String) = Single.fromFuture(
        simpleStore.putString(KEY_SELECTED_PART, partId)
    )

    override fun getSettingSheetScreenOn() =
        Single.fromFuture(simpleStore.getString(KEY_SHEETS_KEEP_SCREEN_ON))
            .map { savedValue ->
                BooleanSetting(
                    KEY_SHEETS_KEEP_SCREEN_ON,
                    R.string.label_setting_screen_on,
                    savedValue.toBoolean()
                )
            }

    override fun getDebugSettingNetworkEndpoint() =
        Single.fromFuture(simpleStore.getString(KEY_DEBUG_NETWORK_ENDPOINT))
            .map { fromStorage ->
                val savedValue = if (fromStorage.isBlank()) {
                    0
                } else {
                    fromStorage.toInt()
                }

                DropdownSetting(
                    KEY_DEBUG_NETWORK_ENDPOINT,
                    R.string.label_debug_network_endpoint,
                    savedValue,
                    NetworkEndpoint.values().map { it.displayStringId }
                )
            }

    override fun saveSettingSheetScreenOn(setting: Boolean) = Single.fromFuture(
        simpleStore.putString(KEY_SHEETS_KEEP_SCREEN_ON, setting.toString())
    )

    override fun getAllSettings(): Single<List<BooleanSetting>> = Single
        .concat(
            // TODO Once there's actually more than one of these, we don't need the listOf call
            listOf(
                getSettingSheetScreenOn()
            )
        )
        .toList()

    override fun getAllDebugSettings(): Single<List<DropdownSetting>> = Single
        .concat(
            // TODO Once there's actually more than one of these, we don't need the listOf call
            listOf(
                getDebugSettingNetworkEndpoint()
            )
        )
        .toList()

    override fun saveSelectedNetworkEndpoint(newValue: Int): Single<String> = Single.fromFuture(
        simpleStore.putString(KEY_DEBUG_NETWORK_ENDPOINT, newValue.toString())
    )

    companion object {
        const val KEY_SELECTED_TOP_LEVEL = "KEY_SELECTED_TOP_LEVEL"
        const val KEY_SELECTED_PART = "KEY_SELECTED_PART"

        const val KEY_SHEETS_KEEP_SCREEN_ON = "SETTING_SHEET_KEEP_SCREEN_ON"

        const val KEY_DEBUG_NETWORK_ENDPOINT = "DEBUG_NETWORK_ENDPOINT"
    }
}
