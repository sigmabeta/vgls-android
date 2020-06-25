package com.vgleadsheets.features.main.settings

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.components.CheckableListModel
import com.vgleadsheets.components.DropdownSettingListModel
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.async.AsyncListViewModel
import com.vgleadsheets.resources.ResourceProvider
import com.vgleadsheets.storage.BooleanSetting
import com.vgleadsheets.storage.DropdownSetting
import com.vgleadsheets.storage.Setting
import com.vgleadsheets.storage.Storage
import timber.log.Timber

class SettingsViewModel @AssistedInject constructor(
    @Assisted initialState: SettingsState,
    private val storage: Storage,
    private val resourceProvider: ResourceProvider
) : AsyncListViewModel<SettingsData, SettingsState>(initialState, resourceProvider),
    CheckableListModel.EventHandler,
    DropdownSettingListModel.EventHandler,
    SingleTextListModel.EventHandler {
    init {
        fetchSettings()
    }

    override fun onClicked(clicked: SingleTextListModel) = setState {
        copy(
            clickedSingleTextModel = clicked
        )
    }

    override fun clearClickedSingleTextModel() = setState {
        copy(
            clickedSingleTextModel = null
        )
    }

    override fun onClicked(clicked: CheckableListModel) {
        setSetting(clicked.settingId, !clicked.checked)
    }

    override fun onNewOptionSelected(settingId: String, selectedPosition: Int) {
        TODO("Implement this!")
    }

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_album_24dp,
        "No settings found at all. What's going on here?"
    )

    override fun createSuccessListModels(
        data: SettingsData,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ): List<ListModel> = createContentListModels(data.settings)

    private fun fetchSettings() = storage
        .getAllSettings()
        .execute { settings ->
            val newData = SettingsData(settings)
            updateListState(
                data = newData,
                listModels = constructList(
                    newData,
                    this
                )
            )
        }

    private fun createContentListModels(
        settings: Async<List<Setting>>
    ) = when (settings) {
        is Success -> createSettingListModels(settings())
        is Fail -> createErrorStateListModel(settings.error)
        is Uninitialized, is Loading -> createLoadingListModels()
    }


    private fun createSettingListModels(settings: List<Setting>): List<ListModel> {
        val sheetsSection = createSection(settings, HEADER_ID_SHEET)
        val miscSection = createMiscSection(settings)

        return sheetsSection + miscSection
    }

    private fun createMiscSection(settings: List<Setting>): List<ListModel> {
        val normalItems = createSection(settings, HEADER_ID_MISC)
        val customItems = listOf(
            SingleTextListModel(
                R.string.label_link_about.toLong(),
                resourceProvider.getString(R.string.label_link_about),
                this
            )
        )

        return normalItems + customItems
    }

    private fun createSection(
        settings: List<Setting>,
        headerId: String
    ): List<ListModel> {
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
                        resourceProvider.getString(setting.labelStringId),
                        setting.value,
                        this
                    )
                    is DropdownSetting -> DropdownSettingListModel(
                        setting.settingId,
                        resourceProvider.getString(setting.labelStringId),
                        setting.selectedPosition,
                        setting.valueStringIds.map { resourceProvider.getString(it) },
                        this
                    )
                }
            }

        return headerModels + settingsModels
    }

    private fun getSectionHeaderString(headerId: String) = when (headerId) {
        HEADER_ID_SHEET -> resourceProvider.getString(R.string.section_sheets)
        HEADER_ID_MISC -> resourceProvider.getString(R.string.section_misc)
        else -> throw IllegalArgumentException()
    }

    private fun setSetting(settingId: String, newValue: Boolean) {
        // TODO These strings need to live in a common module
        val settingSaveOperation = when (settingId) {
            "SETTING_SHEET_KEEP_SCREEN_ON" -> storage.saveSettingSheetScreenOn(newValue)
            else -> throw IllegalArgumentException()
        }

        settingSaveOperation
            .subscribe(
                {
                    fetchSettings()
                },
                {
                    Timber.e("Failed to update setting: ${it.message}")
                }
            )
            .disposeOnClear()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SettingsState): SettingsViewModel
    }

    companion object : MvRxViewModelFactory<SettingsViewModel, SettingsState> {
        const val HEADER_ID_SHEET = "SETTING_SHEET"
        const val HEADER_ID_MISC = "SETTING_MISC"

        override fun create(
            viewModelContext: ViewModelContext,
            state: SettingsState
        ): SettingsViewModel? {
            val fragment: SettingsFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.settingsViewModelFactory.create(state)
        }
    }
}