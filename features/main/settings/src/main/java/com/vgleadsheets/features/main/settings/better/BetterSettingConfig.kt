package com.vgleadsheets.features.main.settings.better

import android.content.res.Resources
import com.vgleadsheets.components.CheckableListModel
import com.vgleadsheets.components.DropdownSettingListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.features.main.list.BetterListConfig
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.content
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.features.main.settings.BuildConfig
import com.vgleadsheets.features.main.settings.R
import com.vgleadsheets.features.main.settings.SettingsViewModel
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.storage.BooleanSetting
import com.vgleadsheets.storage.DropdownSetting
import com.vgleadsheets.storage.Setting

class BetterSettingConfig(
    private val state: BetterSettingState,
    private val viewModel: BetterSettingViewModel,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig<BetterSettingState, BetterSettingClicks> {
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

        val sheetsSection = createSection(settings, SettingsViewModel.HEADER_ID_SHEET)
        val miscSection = createMiscSection(settings)

        sheetsSection + miscSection
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

    private fun createMiscSection(settings: List<Setting>): List<ListModel> {
        val normalItems = createSection(settings, SettingsViewModel.HEADER_ID_MISC)
        val customItems = listOf(
            SingleTextListModel(
                R.string.label_link_about.toLong(),
                resources.getString(R.string.label_link_about),
                onSingleTextClicked()
            )
        )

        return normalItems + customItems
    }

    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    private fun getSectionHeaderString(headerId: String) = when (headerId) {
        SettingsViewModel.HEADER_ID_SHEET -> resources.getString(R.string.section_sheets)
        SettingsViewModel.HEADER_ID_MISC -> resources.getString(R.string.section_misc)
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
        BetterSettingFragment.LOAD_OPERATION,
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
            viewModel.onAboutClicked()
        }

        override fun clearClicked() {}
    }

    private fun onDropdownSettingSelected() = object : DropdownSettingListModel.EventHandler {
        override fun onNewOptionSelected(settingId: String, selectedPosition: Int) {
            viewModel.onDropdownSettingSelected(settingId, selectedPosition)
        }
    }
}
