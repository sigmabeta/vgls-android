package com.vgleadsheets.remaster.menu

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appinfo.AppInfo
import com.vgleadsheets.components.CheckableListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingItemListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.NoopListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider

data class State(
    val keepScreenOn: Boolean? = null,
    val appInfo: AppInfo? = null,
    val formattedBuildDate: String? = null,
    val debugClickCount: Int = 0,
    val shouldShowDebug: Boolean? = null,
    val debugShouldDelay: Boolean? = null,
    val debugShouldShowNavSnackbars: Boolean? = null,
    val songRecordsGenerated: Int? = 0,
    val songRecordsGeneratedLegacy: Int? = 0,
    val songRecordsMigrated: Int? = 0,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_SETTINGS),
        shouldShowBack = true
    )

    override fun toListItems(stringProvider: StringProvider): List<ListModel> = listOfNotNull(
        keepScreenOn(stringProvider),
        sectionHeader(stringProvider.getString(StringId.SECTION_HEADER_SETTINGS_ABOUT)),
        appWhatsNew(stringProvider),
        website(stringProvider),
        giantBomb(stringProvider),
        appVersionName(stringProvider),
        appVersionCode(stringProvider),
        appBuildBranch(stringProvider),
        appBuildDate(stringProvider),
        licenses(stringProvider),
        ifShowDebugEnabled { sectionHeader(stringProvider.getString(StringId.SECTION_HEADER_SETTINGS_DEBUG)) },
        shouldDelay(stringProvider),
        shouldShowNavSnackbars(stringProvider),
        generateUserRecords(stringProvider),
        generateUserRecordsLegacy(stringProvider),
        migrateUserRecordsLegacy(stringProvider),
        restartApp(stringProvider),
    )

    private fun restartApp(stringProvider: StringProvider) = ifShowDebugEnabled {
        SingleTextListModel(
            name = stringProvider.getString(StringId.SETTINGS_LABEL_DEBUG_RESTART),
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
                name = stringProvider.getString(StringId.SETTINGS_LABEL_DEBUG_GENERATE_RECORDS),
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
                name = stringProvider.getString(StringId.SETTINGS_LABEL_DEBUG_GENERATE_RECORDS_LEGACY),
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
                name = stringProvider.getString(StringId.SETTINGS_LABEL_DEBUG_MIGRATE_RECORDS),
                clickAction = Action.MigrateUserContentLegacyClicked
            )
        }
    }

    private fun sectionHeader(title: String) = SectionHeaderListModel(
        title = title
    )

    private fun keepScreenOn(stringProvider: StringProvider) = CheckableListModel(
        name = stringProvider.getString(StringId.SETTINGS_LABEL_KEEP_SCREEN_ON),
        clickAction = Action.KeepScreenOnClicked,
        settingId = StringId.SETTINGS_LABEL_KEEP_SCREEN_ON.name,
        checked = keepScreenOn,
    )

    private fun licenses(stringProvider: StringProvider) = SingleTextListModel(
        name = stringProvider.getString(StringId.SETTINGS_LABEL_LICENSES),
        clickAction = Action.LicensesLinkClicked
    )

    private fun website(stringProvider: StringProvider) = SingleTextListModel(
        name = stringProvider.getString(StringId.SETTINGS_LABEL_WEBSITE),
        clickAction = Action.WebsiteLinkClicked
    )

    private fun giantBomb(stringProvider: StringProvider) = SingleTextListModel(
        name = stringProvider.getString(StringId.SETTINGS_LABEL_GIANT_BOMB),
        clickAction = Action.GiantBombClicked
    )

    private fun appVersionName(stringProvider: StringProvider) = LabelValueListModel(
        label = stringProvider.getString(StringId.SETTINGS_LABEL_APP_VERSION_NAME),
        value = appInfo?.versionName,
        clickAction = VglsAction.Noop
    )

    @Suppress("ReturnCount")
    private fun appVersionCode(stringProvider: StringProvider) = ifShowDebugEnabled {
        val versionCode = appInfo?.versionCode
        val value = versionCode?.let { appInfo?.versionCode.toString() }

        LabelValueListModel(
            label = stringProvider.getString(StringId.SETTINGS_LABEL_APP_VERSION_CODE),
            value = value,
            clickAction = VglsAction.Noop
        )
    }

    private fun appBuildDate(stringProvider: StringProvider) = LabelValueListModel(
        label = stringProvider.getString(StringId.SETTINGS_LABEL_APP_BUILD_DATE),
        value = formattedBuildDate,
        clickAction = Action.BuildDateClicked
    )

    private fun appWhatsNew(stringProvider: StringProvider) = SingleTextListModel(
        name = stringProvider.getString(StringId.SETTINGS_LABEL_APP_WHATS_NEW),
        clickAction = Action.WhatsNewClicked
    )

    private fun appBuildBranch(stringProvider: StringProvider) = ifShowDebugEnabled {
        LabelValueListModel(
            label = stringProvider.getString(StringId.SETTINGS_LABEL_APP_BRANCH),
            value = appInfo?.buildBranch,
            clickAction = VglsAction.Noop
        )
    }

    private fun shouldDelay(stringProvider: StringProvider) = ifShowDebugEnabled {
        CheckableListModel(
            name = stringProvider.getString(StringId.SETTINGS_LABEL_DEBUG_DELAY),
            clickAction = Action.DebugDelayClicked,
            settingId = StringId.SETTINGS_LABEL_DEBUG_DELAY.name,
            checked = debugShouldDelay,
        )
    }

    private fun shouldShowNavSnackbars(stringProvider: StringProvider) = ifShowDebugEnabled {
        CheckableListModel(
            name = stringProvider.getString(StringId.SETTINGS_LABEL_DEBUG_NAV_SNACKBARS),
            clickAction = Action.DebugShowNavSnackbarsClicked,
            settingId = StringId.SETTINGS_LABEL_DEBUG_NAV_SNACKBARS.name,
            checked = debugShouldShowNavSnackbars,
        )
    }

    private fun ifShowDebugEnabled(content: () -> ListModel): ListModel {
        return if (shouldShowDebug == true) {
            content()
        } else {
            NoopListModel
        }
    }
}
