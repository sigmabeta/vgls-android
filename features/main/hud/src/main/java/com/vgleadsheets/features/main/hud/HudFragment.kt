package com.vgleadsheets.features.main.hud

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.features.main.hud.databinding.FragmentHudBinding
import com.vgleadsheets.features.main.hud.menu.HudVisibility
import com.vgleadsheets.features.main.hud.menu.MenuRenderer
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.storage.Storage
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

    private var _binding: FragmentHudBinding? = null

    private val screen: FragmentHudBinding
        get() = _binding!!

    private val viewModel: HudViewModel by activityViewModel()

    private lateinit var menuAdapter: ComponentAdapter

    fun onAppBarButtonClick() = withState(viewModel) {
        clicks.appBarButton(it)
    }

    override fun disablePerfTracking() = true

    override fun getPerfSpec() = PerfSpec.HUD

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        bottomSheetBehavior = BottomSheetBehavior.from(screen.frameBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val recyclerBottom = screen.recyclerBottom

        menuAdapter = ComponentAdapter(getVglsFragmentTag(), hatchet)
        recyclerBottom.adapter = menuAdapter
        recyclerBottom.layoutManager = LinearLayoutManager(context)

        menuAdapter.resources = resources

        screen.shadowHud.setOnClickListener { clicks.shadow() }
    }

    @Suppress("ComplexMethod", "LongMethod")
    override fun invalidate() = withState(viewModel) { state ->
        HudVisibility.setToLookRightIdk(
            screen.shadowHud,
            state.mode,
            bottomSheetBehavior
        )

        if (state.mode != HudMode.SEARCH) {
            val searchText = screen
                .recyclerBottom
                .findViewById<EditText>(R.id.edit_search_query)

            if (searchText != null) {
                searchText.clearFocus()
                hideKeyboard()
            }
        }

        val menuItems = MenuRenderer.renderMenu(
            state.mode,
            state.searchQuery,
            state.searchResults,
            state.activeJam,
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

        menuAdapter.submitListAnimateResizeContainer(
            menuItems,
            screen.frameBottomSheet as ViewGroup
        )

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

    private fun hideKeyboard() {
        val imm = getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    companion object {
        const val TOP_LEVEL_SCREEN_ID_FAVORITES = "FAVORITES"
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
