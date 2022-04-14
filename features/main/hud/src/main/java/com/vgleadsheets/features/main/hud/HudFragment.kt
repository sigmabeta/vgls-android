package com.vgleadsheets.features.main.hud

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.vgleadsheets.Side
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInSlightly
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.slideViewDownOffscreen
import com.vgleadsheets.animation.slideViewOnscreen
import com.vgleadsheets.animation.slideViewUpOffscreen
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.components.MenuTitleBarListModel
import com.vgleadsheets.components.PartListModel
import com.vgleadsheets.components.PerfStageListModel
import com.vgleadsheets.features.main.hud.perf.PerfViewScreenStatus
import com.vgleadsheets.features.main.hud.perf.PerfViewStatus
import com.vgleadsheets.perf.tracking.api.PerfStage
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForMargin
import com.vgleadsheets.setInsetListenerForOnePadding
import com.vgleadsheets.storage.Storage
import com.vgleadsheets.tracking.TrackingScreen
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_hud.button_search_clear
import kotlinx.android.synthetic.main.fragment_hud.button_search_menu_back
import kotlinx.android.synthetic.main.fragment_hud.card_search
import kotlinx.android.synthetic.main.fragment_hud.edit_search_query
import kotlinx.android.synthetic.main.fragment_hud.frame_content
import kotlinx.android.synthetic.main.fragment_hud.shadow_hud
import kotlinx.android.synthetic.main.fragment_hud.text_search_hint
import kotlinx.android.synthetic.main.view_bottom_sheet_card.bottom_sheet
import kotlinx.android.synthetic.main.view_bottom_sheet_content.recycler_bottom
import kotlinx.android.synthetic.main.view_perf_event_list.list_perf
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@Suppress("TooManyFunctions")
class HudFragment : VglsFragment(), PartListModel.ClickListener {
    @Inject
    lateinit var storage: Storage

    private val viewModel: HudViewModel by activityViewModel()

    private val disposables = CompositeDisposable()

    private val menuAdapter = ComponentAdapter()

    private val perfAdapter = ComponentAdapter()

    private val backListener = View.OnClickListener { activity?.onBackPressed() }

    private val menuListener = View.OnClickListener { onMenuClick() }

    private val handler = Handler()

    private val focusRequester = Runnable {
        if (edit_search_query?.alpha == 1.0f) {
            edit_search_query?.requestFocus()

            val imm = ContextCompat.getSystemService(activity!!, InputMethodManager::class.java)
            imm?.showSoftInput(edit_search_query, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun disablePerfTracking() = true

    override fun getFullLoadTargetTime() = -1L

    override fun onClicked(clicked: PartListModel) {
        onPartSelect(clicked)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.selectSubscribe(
                HudState::readyToShowScreens,
                deliveryMode = uniqueOnly("readyToShow")
            ) { ready ->
                if (ready) {
                    showInitialScreen()
                }
            }
        }

        // Configure search bar insets
        card_search.setInsetListenerForMargin(
            offset = resources.getDimension(R.dimen.margin_medium).toInt()
        )

        val cornerOffset = resources.getDimension(R.dimen.margin_small).toInt()

        recycler_bottom.adapter = menuAdapter
        recycler_bottom.layoutManager = LinearLayoutManager(context)
        recycler_bottom.setInsetListenerForOnePadding(Side.BOTTOM, offset = cornerOffset)
        bottom_sheet.updateLayoutParams<FrameLayout.LayoutParams> {
            bottomMargin = -cornerOffset
        }

        checkShouldShowPerfView()

        button_search_clear.setOnClickListener { edit_search_query.text.clear() }
        shadow_hud.setOnClickListener { viewModel.onMenuAction() }

    }

    override fun onStart() {
        super.onStart()
        val textEntryEvents = searchEvents()
            .subscribe {
                viewModel.searchQuery(it)
            }
        disposables.add(textEntryEvents)

        val clickEvents = searchClicks()
            .subscribe {
                withState(viewModel) {
                    if (!it.searchVisible) {
                        viewModel.searchClick()
                    }
                }
            }
        disposables.add(clickEvents)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
        viewModel.clearPerfTimers()
    }

    @Suppress("ComplexMethod", "LongMethod")
    override fun invalidate() = withState(viewModel) { state ->
        if (state.hudVisible) {
            showHud()
        } else {
            hideHud()
        }

        if (state.searchVisible) {
            showSearch()
            showSearchBackButton()
        } else {
            hideSearch()
            showSearchMenuButton()
        }

        renderMenu(
            state.menuExpanded,
            state.updateTime
        )

        if (state.alwaysShowBack) {
            showSearchBackButton()
        }

        if (state.searchQuery.isNullOrEmpty()) {
            hideSearchClearButton()
        } else {
            showSearchClearButton()
        }

        val updatePerfView = state.updatePerfView
        if (updatePerfView is Success && updatePerfView().value) {
//            showPerfStatus(state.perfViewStatus)
        }

        if (state.digest is Loading) {
            perfTracker.cancelAll()
        }
    }

    override fun onBackPress() = withState(viewModel) {
        if (it.menuExpanded) {
            viewModel.onMenuBackPress()
            return@withState true
        }

        return@withState false
    }

    override fun getLayoutId() = R.layout.fragment_hud

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    override fun getTrackingScreen() = TrackingScreen.HUD

    private fun showInitialScreen() {
        Timber.d("Checking to see which screen to show.")
        val screenLoad = storage.getSavedTopLevelScreen().subscribe(
            {
                val selection = if (it.isEmpty()) {
                    TOP_LEVEL_SCREEN_ID_DEFAULT
                } else {
                    it
                }

                Timber.v("Showing screen: $it")
                showScreen(selection, false, false)
            },
            {
                Timber.w("No screen ID found, going with default.")
                showScreen(TOP_LEVEL_SCREEN_ID_DEFAULT, false, false)
            }
        )
        disposables.add(screenLoad)
    }

    @Suppress("ComplexMethod")
    private fun showScreen(screenId: String, fromUserClick: Boolean = true, save: Boolean = true) {
        viewModel.onMenuAction()

        val fromScreen = if (fromUserClick) getTrackingScreen() else null
        val fromDetails = if (fromUserClick) getDetails() else null

        when (screenId) {
            TOP_LEVEL_SCREEN_ID_GAME -> getFragmentRouter().showGameList(fromScreen, fromDetails)
            TOP_LEVEL_SCREEN_ID_COMPOSER -> getFragmentRouter().showComposerList(
                fromScreen,
                fromDetails
            )
            TOP_LEVEL_SCREEN_ID_TAG -> getFragmentRouter().showTagList(fromScreen, fromDetails)
            TOP_LEVEL_SCREEN_ID_SONG -> getFragmentRouter().showAllSheets(fromScreen, fromDetails)
            TOP_LEVEL_SCREEN_ID_JAM -> getFragmentRouter().showJams(fromScreen, fromDetails)
            MODAL_SCREEN_ID_SETTINGS -> getFragmentRouter().showSettings(fromScreen, fromDetails)
            MODAL_SCREEN_ID_DEBUG -> getFragmentRouter().showDebug(fromScreen, fromDetails)
            else -> getFragmentRouter().showGameList(fromScreen, fromDetails)
        }

        if (save) {
            storage.saveTopLevelScreen(screenId)
        }
    }

    private fun onPartSelect(clicked: PartListModel) {
        tracker.logPartSelect(clicked.name)
        viewModel.onPartSelect(clicked.name)

        val save = storage.saveSelectedPart(clicked.name)
            .subscribe({}, { showError("Failed to save part selection: ${it.message}") })
        disposables.add(save)
    }

    private fun onMenuClick() {
        tracker.logMenuShow()
        viewModel.onMenuClick()
    }

    private fun onRandomClick() = withState(viewModel) { state ->
        val selectedPart = state.parts.first { it.selected }
        viewModel.onRandomSelectClick(selectedPart)
    }

    private fun onRefreshClick() = withState(viewModel) {
        if (it.digest !is Loading) {
            tracker.logForceRefresh()
            viewModel.refresh()
        }
    }

    private fun showSearchClearButton() {
        button_search_clear.fadeIn()
    }

    private fun hideSearchClearButton() {
        button_search_clear.fadeOutGone()
    }

    private fun showSearchMenuButton() {
        button_search_menu_back.contentDescription = getString(R.string.cd_search_menu)
        button_search_menu_back.setImageResource(R.drawable.ic_menu_24dp)
        button_search_menu_back.setOnClickListener(menuListener)
    }

    private fun showSearchBackButton() {
        button_search_menu_back.contentDescription = getString(R.string.cd_search_back)
        button_search_menu_back.setImageResource(R.drawable.ic_arrow_back_black_24dp)
        button_search_menu_back.setOnClickListener(backListener)
    }

    private fun showSearch() {
        viewModel.stopHudTimer()

        text_search_hint.fadeOutGone()
        edit_search_query.fadeIn()

        handler.postDelayed(focusRequester, DELAY_HALF_SECOND)

        getFragmentRouter().showSearch()
    }

    private fun hideSearch() {
        // TODO Delay the text clearing
        edit_search_query.text.clear()
        edit_search_query.fadeOutGone()
        text_search_hint.fadeIn()

        handler.removeCallbacks(focusRequester)

        val imm = ContextCompat.getSystemService(activity!!, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(edit_search_query.windowToken, 0)
    }

    private fun showHud() {
        card_search.animate().cancel()
        bottom_sheet.animate().cancel()

        card_search.slideViewOnscreen()
        bottom_sheet.slideViewOnscreen()

        view?.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    private fun hideHud() {
        if (card_search.visibility != GONE) {
            card_search.slideViewUpOffscreen()
            bottom_sheet.slideViewDownOffscreen()

            view?.systemUiVisibility = SYSTEM_UI_FLAG_IMMERSIVE or
                    SYSTEM_UI_FLAG_FULLSCREEN or
                    SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    private fun renderMenu(visible: Boolean, updateTime: Async<Long>) {
        if (!visible) {
            shadow_hud.fadeOutGone()

            menuAdapter.submitListAnimateResizeContainer(
                listOf(
                    MenuTitleBarListModel(
                        getString(R.string.app_name),
                        false,
                        { viewModel.onMenuClick() },
                        "",
                        perfTracker
                    )
                ),
                recycler_bottom?.parent?.parent as? ViewGroup
            )

            return
        }

        shadow_hud.fadeInSlightly()

        val menuItems = listOf(
            MenuTitleBarListModel(
                getString(R.string.app_name),
                true,
                { viewModel.onMenuClick() },
                "",
                perfTracker
            ),
            MenuItemListModel(
                getString(R.string.label_by_game),
                null,
                R.drawable.ic_album_24dp,
                { showScreen(TOP_LEVEL_SCREEN_ID_GAME) },
                "",
                perfTracker
            ),
            MenuItemListModel(
                getString(R.string.label_by_composer),
                null,
                R.drawable.ic_person_24dp,
                { showScreen(TOP_LEVEL_SCREEN_ID_COMPOSER) },
                "",
                perfTracker
            ),
            MenuItemListModel(
                getString(R.string.label_by_tag),
                null,
                R.drawable.ic_tag_black_24dp,
                { showScreen(TOP_LEVEL_SCREEN_ID_TAG) },
                "",
                perfTracker
            ),
            MenuItemListModel(
                getString(R.string.label_all_sheets),
                null,
                R.drawable.ic_description_24dp,
                { showScreen(TOP_LEVEL_SCREEN_ID_SONG) },
                "",
                perfTracker
            ),
            MenuItemListModel(
                getString(R.string.label_random),
                null,
                R.drawable.ic_shuffle_24dp,
                { onRandomClick() },
                "",
                perfTracker
            ),
            MenuItemListModel(
                getString(R.string.label_jams),
                null,
                R.drawable.ic_queue_music_black_24dp,
                { showScreen(TOP_LEVEL_SCREEN_ID_JAM) },
                "",
                perfTracker
            ),
            MenuItemListModel(
                getString(R.string.label_settings),
                null,
                R.drawable.ic_settings_black_24dp,
                { showScreen(MODAL_SCREEN_ID_SETTINGS) },
                "",
                perfTracker
            ),
            MenuItemListModel(
                getString(R.string.label_refresh),
                getUpdateTimeString(updateTime),
                R.drawable.ic_refresh_24dp,
                { onRefreshClick() },
                "",
                perfTracker
            )
        )

        menuAdapter.submitListAnimateResizeContainer(
            menuItems,
            recycler_bottom?.parent?.parent as? ViewGroup
        )
    }

    private fun getUpdateTimeString(updateTime: Async<Long>): String {
        val calendar = Calendar.getInstance()

        if (updateTime !is Success) {
            return "..."
        }

        val checkedTime = updateTime()

        val date = if (checkedTime > 0L) {
            calendar.timeInMillis = checkedTime
            val time = calendar.time
            val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
            dateFormat.format(time)
        } else {
            getString(R.string.date_never)
        }

        return getString(R.string.label_refresh_date, date)
    }

    private fun checkShouldShowPerfView() {
        val showPerf = storage.getDebugSettingShowPerfView()
            .subscribe(
                {
                    if (it.value) {
                        setupPerfView()
                    } else {
                        hidePerfView()
                    }
                },
                {
                    Timber.w("No pref found for showing perf view; hiding.")
                    hidePerfView()
                }
            )

        disposables.add(showPerf)
    }

    private fun hidePerfView() {
        frame_content.removeView(list_perf)
    }

    private fun setupPerfView() {
        list_perf.adapter = perfAdapter
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.stackFromEnd = true
        list_perf.layoutManager = linearLayoutManager
    }

    private fun showPerfStatus(perfViewStatus: PerfViewStatus) {
        val listModels = perfViewStatus.screenStatuses
            .sortedBy { it.startTime }
            .map { getListModelsForPerfScreen(it) }
            .flatten()

        perfAdapter.submitList(listModels)
    }

    private fun getListModelsForPerfScreen(screen: PerfViewScreenStatus) = listOf(
        PerfStageListModel(
            screen.screenName,
            screen.startTime,
            screen.screenName,
            screen.completionDuration?.toString() ?: "...",
            screen.targetTimes["completion"] ?: throw IllegalArgumentException(
                "Missing target time for completion."
            ),
            screen.cancellationDuration
        ),
        createPerfStageListModel(screen, PerfStage.VIEW_CREATED),
        createPerfStageListModel(screen, PerfStage.TITLE_LOADED),
        createPerfStageListModel(screen, PerfStage.TRANSITION_START),
        createPerfStageListModel(screen, PerfStage.PARTIAL_CONTENT_LOAD),
        createPerfStageListModel(screen, PerfStage.FULL_CONTENT_LOAD)
    )

    private fun createPerfStageListModel(
        screen: PerfViewScreenStatus,
        perfStage: PerfStage
    ) = PerfStageListModel(
        screen.screenName,
        screen.startTime,
        perfStage.toString(),
        screen.durations[perfStage.toString()]?.toString() ?: "...",
        screen.targetTimes[perfStage.toString()] ?: throw IllegalArgumentException(
            "Missing target time for stage $perfStage."
        ),
        screen.cancellationDuration
    )

    private fun searchClicks() = card_search.clicks()
        .throttleFirst(THRESHOLD_SEARCH_CLICKS, TimeUnit.MILLISECONDS)

    private fun searchEvents() = edit_search_query
        .afterTextChangeEvents()
        .throttleLast(THRESHOLD_SEARCH_EVENTS, TimeUnit.MILLISECONDS)
        .map { it.editable.toString() }

    companion object {
        const val THRESHOLD_SEARCH_EVENTS = 1500L
        const val THRESHOLD_SEARCH_CLICKS = 200L

        const val DELAY_HALF_SECOND = 500L

        const val TOP_LEVEL_SCREEN_ID_GAME = "GAME"
        const val TOP_LEVEL_SCREEN_ID_COMPOSER = "COMPOSER"
        const val TOP_LEVEL_SCREEN_ID_SONG = "SONG"
        const val TOP_LEVEL_SCREEN_ID_TAG = "TAG"
        const val TOP_LEVEL_SCREEN_ID_JAM = "JAM"

        const val TOP_LEVEL_SCREEN_ID_DEFAULT = TOP_LEVEL_SCREEN_ID_GAME

        const val MODAL_SCREEN_ID_SETTINGS = "SETTINGS"
        const val MODAL_SCREEN_ID_DEBUG = "DEBUG"

        fun newInstance() = HudFragment()
    }
}
