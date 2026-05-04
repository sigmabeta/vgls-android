package com.vgleadsheets.remaster.menu

import com.vgleadsheets.strings.VglsStringId
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appinfo.AppInfo
import net.sigmabeta.sage.components.CheckableListModel
import net.sigmabeta.sage.components.LabelValueListModel
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.LoadingItemListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.NameCaptionListModel
import net.sigmabeta.sage.components.NoopListModel
import net.sigmabeta.sage.components.SectionHeaderListModel
import net.sigmabeta.sage.components.SingleTextListModel
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.ui.StringProvider

data class State(
    val refreshCheckStatus: LCE<Unit> = LCE.Uninitialized,
    val sheetDbClearStatus: LCE<Unit> = LCE.Uninitialized,
    val usageDbClearStatus: LCE<Unit> = LCE.Uninitialized,
    val keepScreenOn: Boolean? = null,
    val appInfo: AppInfo? = null,
    val formattedBuildDate: String? = null,
    val debugClickCount: Int = 0,
    val shouldShowDebug: Boolean? = null,
    val debugShouldUseFakeApi: Boolean? = null,
    val debugShouldDelay: Boolean? = null,
    val debugShouldShowNavSnackbars: Boolean? = null,
    val debugShouldShowRenderOverlay: Boolean? = null,
    val songRecordsGenerated: Int? = 0,
    val songRecordsGeneratedLegacy: Int? = 0,
    val songRecordsMigrated: Int? = 0,
    val offlineDownloadStatus: LCE<Unit> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(VglsStringId.SCREEN_TITLE_SETTINGS),
        shouldShowBack = true
    )

    override fun toListItems(stringProvider: StringProvider): List<ListModel> = listOfNotNull(
        checkVglsForUpdates(stringProvider),
        keepScreenOn(stringProvider),
        sectionHeader(stringProvider.getString(VglsStringId.SECTION_HEADER_SETTINGS_DATA)),
        clearUsageHistory(stringProvider),
        clearSheetDb(stringProvider),
        offlineUpdateHistory(stringProvider),
        sectionHeader(stringProvider.getString(VglsStringId.SECTION_HEADER_SETTINGS_ABOUT)),
        appWhatsNew(stringProvider),
        website(stringProvider),
        giantBomb(stringProvider),
        privacy(stringProvider),
        appVersionName(stringProvider),
        appVersionCode(stringProvider),
        appBuildBranch(stringProvider),
        appBuildDate(stringProvider),
        licenses(stringProvider),
        ifShowDebugEnabled { sectionHeader(stringProvider.getString(VglsStringId.SECTION_HEADER_SETTINGS_DEBUG)) },
        shouldUseFakeApi(stringProvider),
        shouldDelay(stringProvider),
        shouldShowNavSnackbars(stringProvider),
        shouldShowRenderOverlay(stringProvider),
        generateUserRecords(stringProvider),
        generateUserRecordsLegacy(stringProvider),
        migrateUserRecordsLegacy(stringProvider),
        runOfflineDownload(stringProvider),
        restartApp(stringProvider),
    )

    private fun runOfflineDownload(stringProvider: StringProvider) = ifShowDebugEnabled {
        if (offlineDownloadStatus is LCE.Loading) {
            LoadingItemListModel(
                loadingType = LoadingType.SINGLE_TEXT,
                loadOperationName = "offlineDownload",
                loadPositionOffset = 0,
            )
        } else {
            SingleTextListModel(
                name = "Run offline download job",
                clickAction = Action.RunOfflineDownloadClicked,
            )
        }
    }

    private fun restartApp(stringProvider: StringProvider) = ifShowDebugEnabled {
        SingleTextListModel(
            name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_DEBUG_RESTART),
            clickAction = Action.RestartAppClicked
        )
    }

    private fun generateUserRecords(stringProvider: StringProvider) = ifShowDebugEnabled {
        if (songRecordsGenerated == null) {
            LoadingItemListModel(
                loadingType = LoadingType.SINGLE_TEXT,
                loadOperationName = "userRecordGeneration",
                loadPositionOffset = 0,
            )
        } else {
            SingleTextListModel(
                name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_DEBUG_GENERATE_RECORDS),
                clickAction = Action.GenerateUserContentClicked
            )
        }
    }

    private fun generateUserRecordsLegacy(stringProvider: StringProvider) = ifShowDebugEnabled {
        if (songRecordsGeneratedLegacy == null) {
            LoadingItemListModel(
                loadingType = LoadingType.SINGLE_TEXT,
                loadOperationName = "userRecordGenerationLegacy",
                loadPositionOffset = 0,
            )
        } else {
            SingleTextListModel(
                name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_DEBUG_GENERATE_RECORDS_LEGACY),
                clickAction = Action.GenerateUserContentLegacyClicked
            )
        }
    }

    private fun migrateUserRecordsLegacy(stringProvider: StringProvider) = ifShowDebugEnabled {
        if (songRecordsMigrated == null) {
            LoadingItemListModel(
                loadingType = LoadingType.SINGLE_TEXT,
                loadOperationName = "userRecordMigrate",
                loadPositionOffset = 0,
            )
        } else {
            SingleTextListModel(
                name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_DEBUG_MIGRATE_RECORDS),
                clickAction = Action.MigrateUserContentLegacyClicked
            )
        }
    }

    private fun offlineUpdateHistory(stringProvider: StringProvider) = NameCaptionListModel(
        name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_OFFLINE_UPDATES),
        caption = stringProvider.getString(VglsStringId.SETTINGS_CAPTION_OFFLINE_UPDATES),
        clickAction = Action.OfflineUpdatesClicked,
        dataId = VglsStringId.SETTINGS_LABEL_OFFLINE_UPDATES.hashCode().toLong(),
    )

    private fun sectionHeader(title: String) = SectionHeaderListModel(
        title = title
    )

    private fun clearUsageHistory(stringProvider: StringProvider) = when (usageDbClearStatus) {
        is LCE.Loading -> LoadingItemListModel(
            loadingType = LoadingType.SINGLE_TEXT,
            loadOperationName = usageDbClearStatus.operationName,
            loadPositionOffset = 0,
        )

        else -> NameCaptionListModel(
            name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_CLEAR_USAGE),
            caption = stringProvider.getString(VglsStringId.SETTINGS_CAPTION_CLEAR_USAGE),
            clickAction = Action.ClearUsageClicked,
            dataId = VglsStringId.SETTINGS_LABEL_CLEAR_USAGE.hashCode().toLong()
        )
    }

    private fun clearSheetDb(stringProvider: StringProvider) = when (sheetDbClearStatus) {
        is LCE.Loading -> LoadingItemListModel(
            loadingType = LoadingType.SINGLE_TEXT,
            loadOperationName = sheetDbClearStatus.operationName,
            loadPositionOffset = 0,
        )

        else -> NameCaptionListModel(
            name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_CLEAR_SHEETS),
            caption = stringProvider.getString(VglsStringId.SETTINGS_CAPTION_CLEAR_SHEETS),
            clickAction = Action.ClearSheetsClicked,
            dataId = VglsStringId.SETTINGS_LABEL_CLEAR_SHEETS.hashCode().toLong()
        )
    }

    private fun checkVglsForUpdates(stringProvider: StringProvider) = when (refreshCheckStatus) {
        is LCE.Loading -> LoadingItemListModel(
            loadingType = LoadingType.SINGLE_TEXT,
            loadOperationName = "updateCheck",
            loadPositionOffset = 0,
        )

        else -> SingleTextListModel(
            name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_CHECK_FOR_UPDATES),
            clickAction = Action.CheckUpdatesClicked
        )
    }

    private fun keepScreenOn(stringProvider: StringProvider) = CheckableListModel(
        name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_KEEP_SCREEN_ON),
        clickAction = Action.KeepScreenOnClicked,
        settingId = VglsStringId.SETTINGS_LABEL_KEEP_SCREEN_ON.name,
        checked = keepScreenOn,
    )

    private fun licenses(stringProvider: StringProvider) = SingleTextListModel(
        name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_LICENSES),
        clickAction = Action.LicensesLinkClicked
    )

    private fun website(stringProvider: StringProvider) = SingleTextListModel(
        name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_WEBSITE),
        clickAction = Action.WebsiteLinkClicked
    )

    private fun giantBomb(stringProvider: StringProvider) = SingleTextListModel(
        name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_GIANT_BOMB),
        clickAction = Action.GiantBombClicked
    )

    private fun privacy(stringProvider: StringProvider) = SingleTextListModel(
        name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_PRIVACY),
        clickAction = Action.PrivacyLinkClicked
    )

    private fun appVersionName(stringProvider: StringProvider) = LabelValueListModel(
        label = stringProvider.getString(VglsStringId.SETTINGS_LABEL_APP_VERSION_NAME),
        value = appInfo?.versionName,
        clickAction = SageAction.Noop
    )

    @Suppress("ReturnCount")
    private fun appVersionCode(stringProvider: StringProvider) = ifShowDebugEnabled {
        val versionCode = appInfo?.versionCode
        val value = versionCode?.let { appInfo?.versionCode.toString() }

        LabelValueListModel(
            label = stringProvider.getString(VglsStringId.SETTINGS_LABEL_APP_VERSION_CODE),
            value = value,
            clickAction = SageAction.Noop
        )
    }

    private fun appBuildDate(stringProvider: StringProvider) = LabelValueListModel(
        label = stringProvider.getString(VglsStringId.SETTINGS_LABEL_APP_BUILD_DATE),
        value = formattedBuildDate,
        clickAction = Action.BuildDateClicked
    )

    private fun appWhatsNew(stringProvider: StringProvider) = SingleTextListModel(
        name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_APP_WHATS_NEW),
        clickAction = Action.WhatsNewClicked
    )

    private fun appBuildBranch(stringProvider: StringProvider) = ifShowDebugEnabled {
        LabelValueListModel(
            label = stringProvider.getString(VglsStringId.SETTINGS_LABEL_APP_BRANCH),
            value = appInfo?.buildBranch,
            clickAction = SageAction.Noop
        )
    }

    private fun shouldUseFakeApi(stringProvider: StringProvider) = ifShowDebugEnabled {
        CheckableListModel(
            name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_DEBUG_FAKE_API),
            clickAction = Action.FakeApiClicked,
            settingId = VglsStringId.SETTINGS_LABEL_DEBUG_FAKE_API.name,
            checked = debugShouldUseFakeApi,
        )
    }

    private fun shouldDelay(stringProvider: StringProvider) = ifShowDebugEnabled {
        CheckableListModel(
            name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_DEBUG_DELAY),
            clickAction = Action.DebugDelayClicked,
            settingId = VglsStringId.SETTINGS_LABEL_DEBUG_DELAY.name,
            checked = debugShouldDelay,
        )
    }

    private fun shouldShowNavSnackbars(stringProvider: StringProvider) = ifShowDebugEnabled {
        CheckableListModel(
            name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_DEBUG_NAV_SNACKBARS),
            clickAction = Action.DebugShowNavSnackbarsClicked,
            settingId = VglsStringId.SETTINGS_LABEL_DEBUG_NAV_SNACKBARS.name,
            checked = debugShouldShowNavSnackbars,
        )
    }

    private fun shouldShowRenderOverlay(stringProvider: StringProvider) = ifShowDebugEnabled {
        CheckableListModel(
            name = stringProvider.getString(VglsStringId.SETTINGS_LABEL_DEBUG_RENDER_OVERLAY),
            clickAction = Action.DebugRenderOverlayClicked,
            settingId = VglsStringId.SETTINGS_LABEL_DEBUG_RENDER_OVERLAY.name,
            checked = debugShouldShowRenderOverlay,
        )
    }

    private fun ifShowDebugEnabled(content: () -> ListModel): ListModel = if (shouldShowDebug == true) {
            content()
        } else {
            NoopListModel
        }
}
