package com.vgleadsheets.features.main.hud

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.vgleadsheets.IsComposeEnabled
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.databinding.FragmentHudComposeBinding
import com.vgleadsheets.features.main.hud.databinding.FragmentHudRecyclerBinding
import com.vgleadsheets.features.main.hud.menu.HudVisibility
import com.vgleadsheets.features.main.hud.menu.MenuRenderer
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.storage.Storage
import com.vgleadsheets.themes.VglsMaterialMenu
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

@Suppress("TooManyFunctions", "DEPRECATION")
class HudFragment : VglsFragment() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    @Inject
    lateinit var storage: Storage

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    internal lateinit var clicks: Clicks

    private lateinit var screenRecycler: FragmentHudRecyclerBinding

    private lateinit var screenCompose: FragmentHudComposeBinding

    private lateinit var shadowHud: View

    private lateinit var frameBottomSheet: FrameLayout

    private val viewModel: HudViewModel by activityViewModel()

    private lateinit var menuAdapter: ComponentAdapter

    fun onAppBarButtonClick() = withState(viewModel) {
        clicks.appBarButton(it)
    }

    override fun disablePerfTracking() = true

    override fun getPerfSpec() = PerfSpec.HUD

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clicks = Clicks(
            viewModel,
            getFragmentRouter(),
            tracker,
            ""
        )

        if (IsComposeEnabled.WELL_IS_IT) {
            setupCompose(view)
        } else {
            setupRecycler(view)
        }

        bottomSheetBehavior = BottomSheetBehavior.from(frameBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        shadowHud.setOnClickListener { clicks.shadow() }
    }

    @Suppress("ComplexMethod", "LongMethod")
    override fun invalidate() = withState(viewModel) { state ->
        HudVisibility.setToLookRightIdk(
            shadowHud,
            state.mode,
            bottomSheetBehavior
        )

        if (state.mode != HudMode.SEARCH) {
            hideKeyboard()
        }

        val menuItems = MenuRenderer.renderMenu(
            state.mode,
            state.searchQuery,
            state.searchResults,
            state.viewerScreenVisible,
            state.selectedSong?.hasVocals ?: true,
            state.selectedPart,
            state.loadTimeLists,
            state.frameTimeStatsMap,
            state.invalidateStatsMap,
            state.digest is Loading,
            state.updateTime,
            state.selectedSong,
            state.perfViewState,
            baseImageUrl,
            viewModel,
            clicks,
            resources
        )

        if (IsComposeEnabled.WELL_IS_IT) {
            renderContentInCompose(menuItems)
        } else {
            renderContentInRecyclerView(menuItems)
        }

        if (state.digest is Loading) {
            perfTracker.cancelAll()
        }
    }

    private fun renderContentInRecyclerView(menuItems: List<ListModel>) {
        menuAdapter.submitListAnimateResizeContainer(
            menuItems,
            screenRecycler.frameBottomSheet as ViewGroup
        )
    }

    override fun onBackPress() = withState(viewModel) {
        return@withState clicks.back(it)
    }

    override fun getLayoutId() = if (IsComposeEnabled.WELL_IS_IT) {
        R.layout.fragment_hud_compose
    } else {
        R.layout.fragment_hud_recycler
    }

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    override fun getTrackingScreen() = TrackingScreen.HUD

    private fun setupCompose(view: View) {
        screenCompose = FragmentHudComposeBinding.bind(view)
        shadowHud = screenCompose.shadowHud
        frameBottomSheet = screenCompose.frameBottomSheet
    }

    private fun setupRecycler(view: View) {
        screenRecycler = FragmentHudRecyclerBinding.bind(view)
        shadowHud = screenRecycler.shadowHud
        frameBottomSheet = screenRecycler.frameBottomSheet

        val recyclerBottom = screenRecycler.recyclerBottom

        menuAdapter = ComponentAdapter(getVglsFragmentTag(), hatchet)
        recyclerBottom.adapter = menuAdapter
        recyclerBottom.layoutManager = LinearLayoutManager(context)

        menuAdapter.resources = resources
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun renderContentInCompose(menuItems: List<ListModel>) {
        screenCompose.composeBottom.setContent {
            VglsMaterialMenu {
                LazyColumn(
                    modifier = Modifier
                        .animateContentSize()
                ) {
                    items(
                        items = menuItems.toTypedArray(),
                        key = { it.dataId },
                        contentType = { it.layoutId }
                    ) {
                        it.Content(
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    companion object {
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
