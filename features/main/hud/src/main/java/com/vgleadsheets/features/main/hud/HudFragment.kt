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
import com.airbnb.mvrx.Success
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
import com.vgleadsheets.animation.slideViewUpOffscreen
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
import com.vgleadsheets.getYoutubeSearchUrlForQuery
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfState
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForMargin
import com.vgleadsheets.setInsetListenerForOnePadding
import com.vgleadsheets.storage.Storage
import com.vgleadsheets.tracking.TrackingScreen
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import timber.log.Timber

@Suppress("TooManyFunctions", "DEPRECATION")
class HudFragment : VglsFragment() {
    @Inject
    lateinit var storage: Storage

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

        screen.buttonSearchClear.setOnClickListener { screen.editSearchQuery.text.clear() }
        screen.shadowHud.setOnClickListener { viewModel.onMenuAction() }

        screen.buttonSearchMenuBack.setOnClickListener {
            withState(viewModel) {
                if (it.mode != HudMode.REGULAR) {
                    activity?.onBackPressed()
                } else {
                    onMenuClick()
                }
            }
        }
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
                    if (it.mode != HudMode.SEARCH) {
                        viewModel.searchClick()
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
            state.mode != HudMode.REGULAR -> {
                screen.buttonSearchMenuBack.setIcon(SearchIcon.State.CLOSE)
            }
            state.mode == HudMode.SEARCH -> {
                showSearch()
                screen.buttonSearchMenuBack.setIcon(SearchIcon.State.BACK)
            }
            else -> {
                hideSearch()
                screen.buttonSearchMenuBack.setIcon(SearchIcon.State.HAMBURGER)
            }
        }

        if (state.random is Success) {
            onRandomSuccess(state, state.random())
        }

        renderMenu(
            state.mode,
            state.selectedSong?.hasVocals ?: true,
            state.selectedPart,
            state.perfState,
            state.digest is Loading,
            state.random is Loading,
            state.updateTime,
            state.selectedSong,
            state.perfViewState
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
        if (it.mode != HudMode.REGULAR) {
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

    private fun onPartSelect(clicked: Part) {
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
        viewModel.onRandomSelectClick(state.selectedPart)
    }

    private fun onRefreshClick() = withState(viewModel) {
        if (it.digest !is Loading) {
            tracker.logForceRefresh()
            viewModel.refresh()
        }
    }

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
        screen.editSearchQuery.animate().cancel()
        screen.includedBottomSheet.containerCard.animate().cancel()

        screen.editSearchQuery.slideViewOnscreen()
        screen.includedBottomSheet.containerCard.slideViewOnscreen()

        view?.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    private fun hideHud() {
        if (screen.editSearchQuery.visibility != GONE) {
            screen.editSearchQuery.slideViewUpOffscreen()
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
        showVocalsOption: Boolean,
        selectedPart: Part,
        perfState: PerfState?,
        refreshing: Boolean,
        randoming: Boolean,
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
                val query = "${currentSong.gameName} - ${currentSong.name} music"

                val youtubeUrl = getYoutubeSearchUrlForQuery(query)

                getFragmentRouter().goToWebUrl(youtubeUrl)
            }
        }

        if (hudMode == HudMode.REGULAR) {
            viewModel.startHudTimer()
        } else {
            viewModel.stopHudTimer()
        }

        val menuItems = TitleBar.getListModels(
            PartSelectorOption.valueOf(selectedPart.name),
            hudMode != HudMode.REGULAR,
            resources,
            { viewModel.onMenuClick() },
            { viewModel.onChangePartClick() },
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
            { onPartSelect(it) },
            resources,
            selectedPart.apiId
        ) + MenuOptions.getListModels(
            hudMode == HudMode.MENU,
            randoming,
            refreshing,
            updateTime,
            { showScreen(it, save = shouldSaveScreenSelection(it)) },
            { onRandomClick() },
            { onRefreshClick() },
            { showScreen(MODAL_SCREEN_ID_DEBUG, save = false) },
            viewModel::onPerfClick,
            resources,
        ) + PerfDisplay.getListModels(
            hudMode == HudMode.PERF,
            perfViewState,
            perfState,
            { viewModel.setPerfSelectedScreen(it) },
            { viewModel.setPerfViewMode(it) },
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

    private fun shouldSaveScreenSelection(screenId: String) =
        screenId != MODAL_SCREEN_ID_DEBUG && screenId != MODAL_SCREEN_ID_SETTINGS

    private fun onRandomSuccess(
        hudState: HudState,
        song: Song?
    ) {
        viewModel.clearRandom()
        viewModel.onMenuAction()

        if (song == null) {
            showError("Failed to get a random track.")
            viewModel.clearRandom()
            return
        }

        val transposition = hudState.selectedPart.apiId

        tracker.logRandomSongView(
            song.name,
            song.gameName,
            transposition
        )

        getFragmentRouter().showSongViewer(
            song.id,
            song.name,
            song.gameName,
            transposition,
            getTrackingScreen(),
            getDetails()
        )
    }

    private fun searchClicks() = screen.editSearchQuery.clicks()
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
