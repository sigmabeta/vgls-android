package com.vgleadsheets.features.main.hud

import android.annotation.SuppressLint
import android.os.Bundle
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
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.Side
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.slideViewDownOffscreen
import com.vgleadsheets.animation.slideViewOnscreen
import com.vgleadsheets.features.main.hud.databinding.FragmentHudBinding
import com.vgleadsheets.features.main.hud.menu.MenuRenderer
import com.vgleadsheets.features.main.hud.menu.Shadow
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForOnePadding
import com.vgleadsheets.storage.Storage
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named

@Suppress("TooManyFunctions", "DEPRECATION")
class HudFragment : VglsFragment() {
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

    private val menuAdapter = ComponentAdapter()

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

        screen.shadowHud.setOnClickListener { clicks.shadow() }
    }

    @Suppress("ComplexMethod", "LongMethod")
    override fun invalidate() = withState(viewModel) { state ->
        if (state.hudVisible) {
            showHud()
        } else {
            hideHud()
        }

        Shadow.setToLookRightIdk(
            screen.shadowHud,
            state.mode
        )

        val menuItems = MenuRenderer.renderMenu(
            state.mode,
            state.searchQuery,
            state.searchResults,
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
            baseImageUrl,
            getFragmentRouter(),
            viewModel,
            clicks,
            resources
        )

        menuAdapter.submitListAnimateResizeContainer(
            menuItems,
            screen.includedBottomSheet.root as ViewGroup
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

    companion object {
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
