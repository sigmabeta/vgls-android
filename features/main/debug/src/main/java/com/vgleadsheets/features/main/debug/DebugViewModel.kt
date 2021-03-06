package com.vgleadsheets.features.main.debug

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
import com.vgleadsheets.components.LoadingCheckableListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.async.AsyncListViewModel
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider
import com.vgleadsheets.storage.BooleanSetting
import com.vgleadsheets.storage.DropdownSetting
import com.vgleadsheets.storage.Setting
import com.vgleadsheets.storage.Storage
import io.reactivex.Completable
import timber.log.Timber

@SuppressWarnings("TooManyFunctions")
class DebugViewModel @AssistedInject constructor(
    @Assisted initialState: DebugState,
    @Assisted val screenName: String,
    private val storage: Storage,
    private val resourceProvider: ResourceProvider,
    private val repository: Repository,
    private val perfTracker: PerfTracker
) : AsyncListViewModel<DebugData, DebugState>(initialState, screenName, perfTracker),
    DropdownSettingListModel.EventHandler,
    SingleTextListModel.EventHandler, CheckableListModel.EventHandler {
    init {
        fetchDebugSettings()
    }

    override fun onClicked(clicked: SingleTextListModel) {
        when (clicked.dataId.toInt()) {
            R.string.label_database_clear_sheets -> clearSheets()
            R.string.label_database_clear_jams -> clearJams()
            else -> throw java.lang.IllegalArgumentException("Unimplemented debug setting!")
        }
    }

    override fun clearClicked() = Unit

    override fun onClicked(clicked: CheckableListModel) {
        setSetting(clicked.settingId, !clicked.checked)
    }

    override fun onCheckboxLoadComplete(screenName: String) {
        perfTracker.onPartialContentLoad(screenName)
        perfTracker.onFullContentLoad(screenName)
    }

    override fun onNewOptionSelected(settingId: String, selectedPosition: Int) {
        setDropdownSetting(settingId, selectedPosition)
    }

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_album_24dp,
        "No settings found at all. What's going on here?",
        "",
        object : EmptyStateListModel.EventHandler {
            override fun onEmptyStateLoadComplete(screenName: String) = Unit
        }
    )

    override fun createSuccessListModels(
        data: DebugData,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ): List<ListModel> = createContentListModels(data.settings)

    override fun defaultLoadingListModel(index: Int) = LoadingCheckableListModel(
        "allSettings",
        index
    )

    private fun fetchDebugSettings() = storage
        .getAllDebugSettings()
        .execute { settings ->
            if (this.data.settings is Success && settings is Loading) {
                return@execute this
            }

            val newData = DebugData(settings)
            updateListState(
                data = newData,
                listModels = constructList(
                    newData,
                    digest,
                    updateTime,
                    selectedPart
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
        val networkSection = createSection(settings, HEADER_ID_NETWORK)
        val databaseSection = createDatabaseSection(settings)
        val miscSection = createSection(settings, HEADER_ID_MISC)

        return networkSection + databaseSection + miscSection
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
                        screenName,
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
        HEADER_ID_NETWORK -> resourceProvider.getString(R.string.section_network)
        HEADER_ID_DATABASE -> resourceProvider.getString(R.string.section_database)
        HEADER_ID_MISC -> resourceProvider.getString(R.string.section_misc)
        else -> throw IllegalArgumentException()
    }

    private fun createDatabaseSection(settings: List<Setting>): List<ListModel> {
        val normalItems = createSection(settings, HEADER_ID_DATABASE)
        val customItems = listOf(
            SingleTextListModel(
                R.string.label_database_clear_sheets.toLong(),
                resourceProvider.getString(R.string.label_database_clear_sheets),
                this
            ),
            SingleTextListModel(
                R.string.label_database_clear_jams.toLong(),
                resourceProvider.getString(R.string.label_database_clear_jams),
                this
            )
        )

        return normalItems + customItems
    }

    private fun setDropdownSetting(settingId: String, newValue: Int) {
        // TODO These strings need to live in a common module
        val settingSaveOperation = when (settingId) {
            "DEBUG_NETWORK_ENDPOINT" -> storage.saveDebugSelectedNetworkEndpoint(newValue)
            else -> throw IllegalArgumentException()
        }

        settingSaveOperation
            .subscribe(
                {
                    fetchDebugSettings()
                    clearAll()
                    setState { copy(changed = true) }
                },
                {
                    Timber.e("Failed to update setting: ${it.message}")
                }
            )
            .disposeOnClear()
    }

    private fun clearSheets() {
        repository.clearSheets()
            .execute {
                copy(sheetDeletion = it)
            }
    }

    private fun clearJams() {
        repository.clearJams()
            .execute {
                copy(jamDeletion = it)
            }
    }

    private fun clearAll() {
        Completable
            .merge(
                listOf(
                    repository.clearSheets(),
                    repository.clearJams()
                )
            )
            .subscribe()
            .disposeOnClear()
    }

    private fun setSetting(settingId: String, newValue: Boolean) {
        // TODO These strings need to live in a common module
        val settingSaveOperation = when (settingId) {
            "DEBUG_MISC_PERF_VIEW" -> storage.saveDebugSettingPerfView(newValue)
            else -> TODO("Don't know how to save setting $settingId yet!")
        }

        settingSaveOperation
            .subscribe(
                {
                    fetchDebugSettings()
                    setState { copy(changed = true) }
                },
                {
                    Timber.e("Failed to update setting: ${it.message}")
                }
            )
            .disposeOnClear()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: DebugState, screenName: String): DebugViewModel
    }

    companion object : MvRxViewModelFactory<DebugViewModel, DebugState> {
        const val HEADER_ID_NETWORK = "DEBUG_NETWORK"
        const val HEADER_ID_DATABASE = "DEBUG_DATABASE"
        const val HEADER_ID_MISC = "DEBUG_MISC"

        override fun create(
            viewModelContext: ViewModelContext,
            state: DebugState
        ): DebugViewModel? {
            val fragment: DebugFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.debugViewModelFactory.create(state, fragment.getPerfScreenName())
        }
    }
}
