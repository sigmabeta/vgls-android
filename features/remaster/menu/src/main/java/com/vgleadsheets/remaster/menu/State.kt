package com.vgleadsheets.remaster.menu

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appinfo.AppInfo
import com.vgleadsheets.components.CheckableListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Part
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import java.util.Locale
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

data class State(
    val selectedPart: Part? = null,
    val keepScreenOn: Boolean? = null,
    val appInfo: AppInfo? = null,
    val debugShouldDelay: Boolean? = null,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_SETTINGS),
        shouldShowBack = true
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> = listOfNotNull(
        keepScreenOn(stringProvider),
        licenses(stringProvider),
        sectionHeader(stringProvider.getString(StringId.SECTION_HEADER_SETTINGS_ABOUT)),
        website(stringProvider),
        giantBomb(stringProvider),
        appVersionName(stringProvider),
        appVersionCode(stringProvider),
        appBuildDate(stringProvider),
        sectionHeader(stringProvider.getString(StringId.SECTION_HEADER_SETTINGS_DEBUG)),
        shouldDelay(stringProvider),
        restartApp(stringProvider),
    ).toImmutableList()

    private fun restartApp(stringProvider: StringProvider) = SingleTextListModel(
        name = stringProvider.getString(StringId.SETTINGS_LABEL_DEBUG_RESTART),
        clickAction = Action.RestartAppClicked
    )

    private fun sectionHeader(title: String) = SectionHeaderListModel(
        title = title
    )

    private fun keepScreenOn(stringProvider: StringProvider) = CheckableListModel(
        name = stringProvider.getString(StringId.SETTINGS_LABEL_KEEP_SCREEN_ON),
        clickAction = Action.KeepScreenOnClicked,
        settingId = StringId.SETTINGS_LABEL_KEEP_SCREEN_ON.name,
        checked = keepScreenOn ?: false,
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

    private fun appVersionName(stringProvider: StringProvider): LabelValueListModel? {
        return LabelValueListModel(
            label = stringProvider.getString(StringId.SETTINGS_LABEL_APP_VERSION_NAME),
            value = appInfo?.versionName ?: return null,
            clickAction = VglsAction.Noop
        )
    }

    @Suppress("ReturnCount")
    private fun appVersionCode(stringProvider: StringProvider): LabelValueListModel? {
        if (appInfo?.isDebug == false) return null

        return LabelValueListModel(
            label = stringProvider.getString(StringId.SETTINGS_LABEL_APP_VERSION_CODE),
            value = appInfo?.versionCode?.toString() ?: return null,
            clickAction = VglsAction.Noop
        )
    }

    private fun appBuildDate(stringProvider: StringProvider): LabelValueListModel? {
        return LabelValueListModel(
            label = stringProvider.getString(StringId.SETTINGS_LABEL_APP_BUILD_DATE),
            value = appInfo?.buildTimeMs?.toBuildDate() ?: return null,
            clickAction = VglsAction.Noop
        )
    }

    private fun shouldDelay(stringProvider: StringProvider) = CheckableListModel(
        name = stringProvider.getString(StringId.SETTINGS_LABEL_DEBUG_DELAY),
        clickAction = Action.DebugDelayClicked,
        settingId = StringId.SETTINGS_LABEL_DEBUG_DELAY.name,
        checked = debugShouldDelay ?: false,
    )

    private fun Long.toBuildDate(): String {
        val instant = Instant.ofEpochMilli(this)
        val formatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.LONG)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())

        if (this == 0L) return formatter.format(Instant.now())

        return formatter.format(instant)
    }
}
