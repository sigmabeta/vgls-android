package com.vgleadsheets.features.main.debug

import android.content.res.Resources
import com.vgleadsheets.components.*
import com.vgleadsheets.features.main.list.BetterListConfig
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.content
import com.vgleadsheets.features.main.list.sections.*
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.storage.BooleanSetting
import com.vgleadsheets.storage.DropdownSetting
import com.vgleadsheets.storage.Setting

internal class Config(
    private val state: DebugState,
    private val clicks: Clicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig<DebugState, Clicks> {
    private val settingsLoad = state.contentLoad.settings

    private val settings = settingsLoad.content()

    override val titleConfig = Title.Config(
        "",
        "",
        resources,
        { },
        { },
        shouldShow = false,
    )

    override val actionsConfig = Actions.NONE

    override val contentConfig = Content.Config(
        !settings.isNullOrEmpty()
    ) {
        perfTracker.onTitleLoaded(perfSpec)
        perfTracker.onTransitionStarted(perfSpec)

        settings ?: return@Config emptyList()

        val networkSection = createSection(settings, HEADER_ID_NETWORK)
        val databaseSection = createDatabaseSection(settings)
        val miscSection = createSection(settings, HEADER_ID_MISC)

        networkSection + databaseSection + miscSection
    }

    override val emptyConfig = EmptyState.Config(
        false,
        0,
        ""
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        DebugFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    private fun createSection(settings: List<Setting>, headerId: String): List<ListModel> {
        val headerModels = listOf(
            SectionHeaderListModel(
                getSectionHeaderString(headerId)
            )
        )

        val settingsModels = settings
            .filter { it.settingId.startsWith(headerId) }
            .map { setting ->
                when (setting) {
                    is BooleanSetting -> CheckableListModel(
                        setting.settingId,
                        resources.getString(setting.labelStringId),
                        setting.value
                    ) { clicks.boolean(setting.settingId, !setting.value) }
                    is DropdownSetting -> DropdownSettingListModel(
                        setting.settingId,
                        resources.getString(setting.labelStringId),
                        setting.selectedPosition,
                        setting.valueStringIds.map { resources.getString(it) }
                    ) { selectedPos ->
                        clicks.dropdownSelection(
                            setting.settingId,
                            selectedPos
                        )
                    }
                }
            }

        return headerModels + settingsModels
    }

    private fun createDatabaseSection(settings: List<Setting>): List<ListModel> {
        val normalItems = createSection(settings, HEADER_ID_DATABASE)
        val customItems = listOf(
            SingleTextListModel(
                R.string.label_database_clear_sheets.toLong(),
                resources.getString(R.string.label_database_clear_sheets)
            ) { clicks.clearSheets() },
            SingleTextListModel(
                R.string.label_database_clear_jams.toLong(),
                resources.getString(R.string.label_database_clear_jams),
            ) { clicks.clearJams() }
        )

        return normalItems + customItems
    }

    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    private fun getSectionHeaderString(headerId: String) = when (headerId) {
        HEADER_ID_NETWORK -> resources.getString(R.string.section_network)
        HEADER_ID_DATABASE -> resources.getString(R.string.section_database)
        HEADER_ID_MISC -> resources.getString(R.string.section_misc)
        else -> throw IllegalArgumentException()
    }

    companion object {
        const val HEADER_ID_NETWORK = "DEBUG_NETWORK"
        const val HEADER_ID_DATABASE = "DEBUG_DATABASE"
        const val HEADER_ID_MISC = "DEBUG_MISC"
    }
}
