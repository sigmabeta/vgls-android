package com.vgleadsheets.features.main.settings.better

import android.content.res.Resources
import com.vgleadsheets.components.CheckableListModel
import com.vgleadsheets.components.DropdownSettingListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.features.main.debug.BuildConfig
import com.vgleadsheets.features.main.debug.DebugViewModel
import com.vgleadsheets.features.main.debug.R
import com.vgleadsheets.features.main.list.BetterListConfig
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.content
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.storage.BooleanSetting
import com.vgleadsheets.storage.DropdownSetting
import com.vgleadsheets.storage.Setting

class BetterDebugConfig(
    private val state: BetterDebugState,
    private val viewModel: BetterDebugViewModel,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig<BetterDebugState, BetterDebugClicks> {
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

        val networkSection = createSection(settings, DebugViewModel.HEADER_ID_NETWORK)
        val databaseSection = createDatabaseSection(settings)
        val miscSection = createSection(settings, DebugViewModel.HEADER_ID_MISC)

        networkSection + databaseSection + miscSection
    }

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
                        setting.value,
                        onCheckboxClicked()
                    )
                    is DropdownSetting -> DropdownSettingListModel(
                        setting.settingId,
                        resources.getString(setting.labelStringId),
                        setting.selectedPosition,
                        setting.valueStringIds.map { resources.getString(it) },
                        onDropdownSettingSelected()
                    )
                }
            }

        return headerModels + settingsModels
    }

    private fun createDatabaseSection(settings: List<Setting>): List<ListModel> {
        val normalItems = createSection(settings, DebugViewModel.HEADER_ID_DATABASE)
        val customItems = listOf(
            SingleTextListModel(
                R.string.label_database_clear_sheets.toLong(),
                resources.getString(R.string.label_database_clear_sheets),
                onSingleTextClicked()
            ),
            SingleTextListModel(
                R.string.label_database_clear_jams.toLong(),
                resources.getString(R.string.label_database_clear_jams),
                onSingleTextClicked()
            )
        )

        return normalItems + customItems
    }

    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    private fun getSectionHeaderString(headerId: String) = when (headerId) {
        DebugViewModel.HEADER_ID_NETWORK -> resources.getString(R.string.section_network)
        DebugViewModel.HEADER_ID_DATABASE -> resources.getString(R.string.section_database)
        DebugViewModel.HEADER_ID_MISC -> resources.getString(R.string.section_misc)
        else -> throw IllegalArgumentException()
    }

    override val emptyConfig = EmptyState.Config(
        false,
        0,
        ""
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        BetterDebugFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    private fun onCheckboxClicked() =
        object : CheckableListModel.EventHandler {
            override fun onClicked(clicked: CheckableListModel) {
                viewModel.onBooleanSettingClicked(
                    clicked.settingId,
                    !clicked.checked,
                )
            }
        }

    private fun onSingleTextClicked() = object : SingleTextListModel.EventHandler {
        override fun onClicked(clicked: SingleTextListModel) {
            when (clicked.dataId.toInt()) {
                R.string.label_database_clear_sheets -> viewModel.clearSheets()
                R.string.label_database_clear_jams -> viewModel.clearJams()
                else -> throw java.lang.IllegalArgumentException("Unimplemented debug setting!")
            }
        }

        override fun clearClicked() {}
    }

    private fun onDropdownSettingSelected() = object : DropdownSettingListModel.EventHandler {
        override fun onNewOptionSelected(settingId: String, selectedPosition: Int) {
            viewModel.onDropdownSettingSelected(settingId, selectedPosition)
        }
    }
}
