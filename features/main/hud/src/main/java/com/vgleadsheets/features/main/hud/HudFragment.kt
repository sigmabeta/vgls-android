package com.vgleadsheets.features.main.hud

import android.annotation.SuppressLint
import android.content.Context
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
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.databinding.FragmentHudComposeBinding
import com.vgleadsheets.features.main.hud.databinding.FragmentHudRecyclerBinding
import com.vgleadsheets.features.main.hud.menu.HudVisibility
import com.vgleadsheets.features.main.hud.menu.MenuRenderer
import com.vgleadsheets.nav.HudMode
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.storage.Storage
import com.vgleadsheets.tracking.TrackingScreen
import com.vgleadsheets.ui.themes.VglsMaterialMenu
import javax.inject.Inject
import javax.inject.Named

@Suppress("TooManyFunctions", "DEPRECATION")
class HudFragment : VglsFragment() {
    // THIS IS A DUMMY INJECTION. If this isn't here, ListFragment_MembersInjector.java
    // doesn't get generated, which causes it to get generated multiple times in children
    // modules, which causes R8 to fail.
    @Inject
    lateinit var dummyContext: Context

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    @Inject
    lateinit var storage: Storage

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    private lateinit var clicks: Clicks

    private lateinit var screenRecycler: FragmentHudRecyclerBinding

    private lateinit var screenCompose: FragmentHudComposeBinding

    private lateinit var shadowHud: View

    private lateinit var frameBottomSheet: FrameLayout

    private val viewModel: HudViewModel by activityViewModel()

    private val navViewModel: NavViewModel by activityViewModel()

    private lateinit var menuAdapter: ComponentAdapter

    fun onAppBarButtonClick() = withState(navViewModel) {
        clicks.appBarButton(it)
    }

    override fun disablePerfTracking() = true

    override fun getPerfSpec() = PerfSpec.HUD

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clicks = Clicks(
            viewModel,
            navViewModel,
            getFragmentRouter(),
            tracker,
            ""
        )

        setupRecycler(view)

        bottomSheetBehavior = BottomSheetBehavior.from(frameBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        shadowHud.setOnClickListener { clicks.shadow() }
    }

    @Suppress("ComplexMethod", "LongMethod")
    override fun invalidate() = withState(viewModel, navViewModel) { state, navState ->
        HudVisibility.setToLookRightIdk(
            shadowHud,
            navState.hudMode,
            bottomSheetBehavior
        )

        if (navState.hudMode != HudMode.SEARCH) {
            hideKeyboard()
        }

        val menuItems = MenuRenderer.renderMenu(
            navState.hudMode,
            state.searchQuery,
            state.searchResults,
            navState.selectedSong?.hasVocals ?: true,
            navState.selectedPart,
            navState.loadTimeLists,
            navState.frameTimeStatsMap,
            navState.invalidateStatsMap,
            navState.digest is Loading,
            navState.updateTime,
            navState.selectedSong,
            navState.perfViewState,
            baseImageUrl,
            navViewModel,
            clicks,
            resources
        )


        renderContentInRecyclerView(menuItems)

        if (navState.digest is Loading) {
            perfTracker.cancelAll()
        }
    }

    private fun renderContentInRecyclerView(menuItems: List<ListModel>) {
        menuAdapter.submitListAnimateResizeContainer(
            menuItems,
            screenRecycler.frameBottomSheet as ViewGroup
        )
    }

    override fun onBackPress() = withState(navViewModel) {
        return@withState clicks.back(it)
    }

    override fun getLayoutId() = R.layout.fragment_hud_recycler

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
        fun newInstance() = HudFragment()
    }
}
