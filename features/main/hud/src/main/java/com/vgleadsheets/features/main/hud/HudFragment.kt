package com.vgleadsheets.features.main.hud

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
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
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.vgleadsheets.Side
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.slideViewDownOffscreen
import com.vgleadsheets.animation.slideViewOnscreen
import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.features.main.hud.databinding.FragmentHudBinding
import com.vgleadsheets.features.main.hud.menu.MenuOptions
import com.vgleadsheets.features.main.hud.menu.PartPicker
import com.vgleadsheets.features.main.hud.menu.PerfDisplay
import com.vgleadsheets.features.main.hud.menu.RefreshIndicator
import com.vgleadsheets.features.main.hud.menu.SearchIcon
import com.vgleadsheets.features.main.hud.menu.SearchIcon.setIcon
import com.vgleadsheets.features.main.hud.menu.Shadow
import com.vgleadsheets.features.main.hud.menu.SheetOptions
import com.vgleadsheets.features.main.hud.menu.SongDisplay
import com.vgleadsheets.features.main.hud.menu.TitleBar
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.perf.tracking.api.FrameTimeStats
import com.vgleadsheets.perf.tracking.api.InvalidateStats
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.ScreenLoadStatus
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForMargin
import com.vgleadsheets.setInsetListenerForOnePadding
import com.vgleadsheets.storage.Storage
import com.vgleadsheets.tracking.TrackingScreen
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("TooManyFunctions", "DEPRECATION")
class HudFragment : VglsFragment() {
    @Inject
    lateinit var storage: Storage

    internal lateinit var clicks: Clicks

    private var _binding: FragmentHudBinding? = null

    private val screen: FragmentHudBinding
        get() = _binding!!

    private val viewModel: HudViewModel by activityViewModel()

    private val disposables = CompositeDisposable()

    private val menuAdapter = ComponentAdapter()

    private val handler = Handler()

    private val focusRequester = Runnable {
        if (screen.editSearchQuery.alpha == 1.0f) {
            screen.editSearchQuery.requestFocus()

            val imm =
                ContextCompat.getSystemService(requireActivity(), InputMethodManager::class.java)
            imm?.showSoftInput(screen.editSearchQuery, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun onAppBarButtonClick() = withState(viewModel) {
        clicks.appBarButton(it)
    }

    override fun disablePerfTracking() = true

    override fun getPerfSpec() = PerfSpec.HUD

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout {
        _binding = FragmentHudBinding.inflate(inflater)
        return screen.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clicks = Clicks(
            viewModel,
            getFragmentRouter(),
            tracker,
            ""
        )

        // Configure search bar insets
        screen.cardSearch.setInsetListenerForMargin(
            offset = resources.getDimension(R.dimen.margin_medium).toInt()
        )

        val cornerOffset = resources.getDimension(R.dimen.margin_small).toInt()

        val recyclerBottom = screen.includedBottomSheet.includedBottomSheetContent.recyclerBottom
        val cardBottomSheet = screen.includedBottomSheet.containerCard

        recyclerBottom.adapter = menuAdapter
        recyclerBottom.layoutManager = LinearLayoutManager(context)
        recyclerBottom.setInsetListenerForOnePadding(Side.BOTTOM, offset = cornerOffset)
        cardBottomSheet.updateLayoutParams<FrameLayout.LayoutParams> {
            bottomMargin = -cornerOffset
        }

        menuAdapter.resources = resources

        screen.buttonSearchClear.setOnClickListener { screen.editSearchQuery.text.clear() }
        screen.shadowHud.setOnClickListener { clicks.shadow() }
        screen.buttonSearchMenuBack.setOnClickListener {
            onAppBarButtonClick()
        }
    }

    override fun onStart() {
        super.onStart()
        val textEntryEvents = searchEvents()
            .subscribe {
                clicks.searchQuery(it)
            }
        disposables.add(textEntryEvents)

        val clickEvents = searchClicks()
            .subscribe {
                withState(viewModel) {
                    if (it.mode != HudMode.SEARCH) {
                        clicks.searchBox()
                    }
                }
            }
        disposables.add(clickEvents)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    @Suppress("ComplexMethod", "LongMethod")
    override fun invalidate() = withState(viewModel) { state ->
        if (state.hudVisible) {
            showHud()
        } else {
            hideHud()
        }

        when {
            state.mode == HudMode.SEARCH -> {
                showSearch()
                screen.buttonSearchMenuBack.setIcon(SearchIcon.State.BACK)
            }
            state.mode != HudMode.REGULAR -> {
                screen.buttonSearchMenuBack.setIcon(SearchIcon.State.CLOSE)
            }
            else -> {
                hideSearch()
                screen.buttonSearchMenuBack.setIcon(SearchIcon.State.HAMBURGER)
            }
        }

        renderMenu(
            state.mode,
            state.selectedSong != null,
            state.selectedSong?.hasVocals ?: true,
            state.selectedPart,
            state.loadTimeLists,
            state.frameTimeStatsMap,
            state.invalidateStatsMap,
            state.digest is Loading,
            state.updateTime,
            state.selectedSong,
            state.perfViewState,
        )

        if (state.alwaysShowBack) {
            screen.buttonSearchMenuBack.setIcon(SearchIcon.State.BACK)
        }

        if (state.searchQuery.isNullOrEmpty()) {
            hideSearchClearButton()
        } else {
            showSearchClearButton()
        }

        if (state.digest is Loading) {
            perfTracker.cancelAll()
        }
    }

    override fun onBackPress() = withState(viewModel) {
        return@withState clicks.back(it)
    }

    override fun getLayoutId() = R.layout.fragment_hud

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    override fun getTrackingScreen() = TrackingScreen.HUD

    private fun showSearchClearButton() {
        screen.buttonSearchClear.fadeIn()
    }

    private fun hideSearchClearButton() {
        screen.buttonSearchClear.fadeOutGone()
    }

    private fun showSearch() {
        screen.textSearchHint.fadeOutGone()
        screen.editSearchQuery.fadeIn()

        handler.postDelayed(focusRequester, DELAY_HALF_SECOND)

        getFragmentRouter().showSearch()
    }

    private fun hideSearch() {
        // TODO Delay the text clearing
        screen.editSearchQuery.text.clear()
        screen.editSearchQuery.fadeOutGone()
        screen.textSearchHint.fadeIn()

        handler.removeCallbacks(focusRequester)

        val imm = ContextCompat.getSystemService(requireActivity(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(screen.editSearchQuery.windowToken, 0)
    }

    private fun showHud() {
        screen.includedBottomSheet.containerCard.slideViewOnscreen()

        view?.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    private fun hideHud() {
        if (screen.includedBottomSheet.containerCard.visibility != GONE) {
            screen.includedBottomSheet.containerCard.slideViewDownOffscreen()

            view?.systemUiVisibility = SYSTEM_UI_FLAG_IMMERSIVE or
                SYSTEM_UI_FLAG_FULLSCREEN or
                SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    @Suppress("LongParameterList", "LongMethod")
    private fun renderMenu(
        hudMode: HudMode,
        shouldHide: Boolean,
        showVocalsOption: Boolean,
        selectedPart: Part,
        loadTimeLists: Map<PerfSpec, ScreenLoadStatus>?,
        frameTimeStatsMap: Map<PerfSpec, FrameTimeStats>?,
        invalidateStatsMap: Map<PerfSpec, InvalidateStats>?,
        refreshing: Boolean,
        updateTime: Async<Long>,
        currentSong: Song?,
        perfViewState: PerfViewState
    ) {
        Shadow.setToLookRightIdk(
            screen.shadowHud,
            hudMode
        )

        val songDetailClickHandler = {
            if (currentSong != null) {
                getFragmentRouter().showSheetDetail(currentSong.id)
            }
        }

        val youtubeSearchClickHandler = {
            if (currentSong != null) {
                getFragmentRouter().searchYoutube(currentSong.name, currentSong.gameName)
            }
        }

        if (hudMode == HudMode.REGULAR && shouldHide) {
            viewModel.startHudTimer()
        } else {
            viewModel.stopHudTimer()
        }

        val menuItems = TitleBar.getListModels(
            PartSelectorOption.valueOf(selectedPart.name),
            hudMode,
            resources,
            { clicks.bottomMenuButton(hudMode, perfViewState.viewMode) },
            { clicks.changePart() },
        ) + SongDisplay.getListModels(
            currentSong,
            songDetailClickHandler,
        ) + SheetOptions.getListModels(
            hudMode == HudMode.REGULAR,
            currentSong,
            songDetailClickHandler,
            youtubeSearchClickHandler,
            resources
        ) + PartPicker.getListModels(
            hudMode == HudMode.PARTS,
            showVocalsOption,
            { clicks.part(it.name) },
            resources,
            selectedPart.apiId
        ) + MenuOptions.getListModels(
            hudMode == HudMode.MENU,
            refreshing,
            updateTime,
            { clicks.screenLink(it) },
            { clicks.randomSelect(selectedPart) },
            { clicks.refresh() },
            { clicks.screenLink(MODAL_SCREEN_ID_DEBUG) },
            { clicks.perf() },
            resources,
        ) + PerfDisplay.getListModels(
            hudMode == HudMode.PERF,
            perfViewState,
            loadTimeLists,
            frameTimeStatsMap,
            invalidateStatsMap,
            { clicks.perfScreenSelection(it) },
            { clicks.setPerfViewMode(it) },
            resources,
        ) + RefreshIndicator.getListModels(
            refreshing,
            resources,
        )

        menuAdapter.submitListAnimateResizeContainer(
            menuItems,
            screen.includedBottomSheet.root as ViewGroup
        )
    }

    private fun searchClicks() = screen.cardSearch.clicks()
        .throttleFirst(THRESHOLD_SEARCH_CLICKS, TimeUnit.MILLISECONDS)

    private fun searchEvents() = screen.editSearchQuery
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
