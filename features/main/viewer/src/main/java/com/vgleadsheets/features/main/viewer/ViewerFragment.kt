package com.vgleadsheets.features.main.viewer

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.args
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.snackbar.Snackbar
import com.vgleadsheets.FragmentInterface
import com.vgleadsheets.Side
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.slideViewOnscreen
import com.vgleadsheets.animation.slideViewUpOffscreen
import com.vgleadsheets.args.ViewerArgs
import com.vgleadsheets.components.SheetListModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.model.pages.Page
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForOneMargin
import com.vgleadsheets.tracking.TrackingScreen
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class ViewerFragment :
    VglsFragment(),
    SheetListModel.ImageListener {
    @Inject
    lateinit var viewerViewModelFactory: ViewerViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    @Inject
    lateinit var dispatchers: VglsDispatchers

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: ViewerViewModel by fragmentViewModel()

    private val viewerArgs: ViewerArgs by args()

    // Keep a local copy for the fragment tag.
    private var songId: Long? = null

    private val sheetsAdapter = ComponentAdapter("ViewerFragment")

    private lateinit var appButton: ImageView

    private var screenOffSnack: Snackbar? = null

    private val onScreenOffSnackClick = View.OnClickListener {
        postInvalidate()
    }

    fun updateSongId(songId: Long) {
        viewModel.updateSongId(songId)
    }

    override fun onClicked() = withState(hudViewModel) { state ->
        if (state.mode != HudMode.HIDDEN) {
            hudViewModel.hideHud()
        } else {
            hudViewModel.showHud()
        }
    }

    override fun onLoadStarted() {
        perfTracker.onTitleLoaded(getPerfSpec())
    }

    override fun onLoadComplete() {
        perfTracker.onTransitionStarted(getPerfSpec())
        perfTracker.onPartialContentLoad(getPerfSpec())
        perfTracker.onFullContentLoad(getPerfSpec())
    }

    override fun onLoadFailed(imageUrl: String, ex: Exception?) {
        perfTracker.cancel(getPerfSpec())
        showError("Image load failed: ${ex?.message ?: "Unknown Error"}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appButton = view.findViewById(R.id.button_app_menu)

        appButton.setInsetListenerForOneMargin(Side.TOP)
        appButton.setOnClickListener { (activity as FragmentInterface).onAppBarButtonClick() }

        hudViewModel.setPerfSelectedScreen(getPerfSpec())

        val sheetsAsPager = view.findViewById<ViewPager2>(R.id.pager_sheets)
        val sheetsAsScrollingList = view.findViewById<RecyclerView>(R.id.list_sheets)

        sheetsAsPager?.adapter = sheetsAdapter

        sheetsAsScrollingList?.adapter = sheetsAdapter
        sheetsAsScrollingList?.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        sheetsAdapter.resources = resources

        viewModel.screenControlEvents
            .onEach {
                when (it) {
                    ScreenControlEvent.TIMER_START -> setScreenOnLock()
                    ScreenControlEvent.TIMER_EXPIRED -> clearScreenOnLock()
                }
            }
            .flowOn(dispatchers.main)
            .launchIn(viewModel.viewModelScope)

        viewModel.onEach(
            ViewerState::activeJamSheetId,
            deliveryMode = uniqueOnly("sheet")
        ) {
            if (it != null) {
                if (songId != null) {
                    showSnackbar(
                        getString(R.string.jam_updating_sheet)
                    )
                }
                updateSongId(it)
            }
        }

        viewModel.onEach(ViewerState::songId) {
            if (it != null) {
                viewModel.fetchSong()
            }
        }

        viewModel.onEach(ViewerState::jamCancellationReason) {
            if (it != null) {
                showError("Jam unfollowed: $it")
                viewModel.clearCancellationReason()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkScreenSetting()
        viewModel.followJam()
    }

    override fun onStop() {
        super.onStop()
        stopScreenTimer()
        hudViewModel.showHud()
        hudViewModel.stopHudTimer()
        viewModel.unfollowJam(null)

        hudViewModel.clearSelectedSong()
        hudViewModel.setViewerScreenNotVisible()
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, viewerState ->
        hudViewModel.alwaysShowBack()

        stopScreenTimer()
        hideScreenOffSnackbar()

        if (viewerState.screenOn is Success && viewerState.screenOn()?.value == false) {
            startScreenTimer()
        }

        if (hudState.mode != HudMode.HIDDEN) {
            windowInsetController?.show(WindowInsetsCompat.Type.systemBars())
            appButton.slideViewOnscreen()

            if (hudState.mode == HudMode.REGULAR) {
                hudViewModel.startHudTimer()
            }
        } else {
            windowInsetController?.hide(WindowInsetsCompat.Type.systemBars())
            appButton.slideViewUpOffscreen()
        }

        val selectedPart = hudState.selectedPart

        songId = viewerState.songId

        when (val song = viewerState.song) {
            is Fail -> showError(
                song.error.message ?: song.error::class.simpleName ?: "Unknown Error"
            )
            is Success -> showSong(viewerState.song(), selectedPart)
            is Loading -> Unit
            Uninitialized -> Unit
        }
    }

    override fun onBackPress() = withState(hudViewModel) { hudState ->
        if (hudState.mode != HudMode.HIDDEN) {
            return@withState false
        } else {
            hudViewModel.showHud()
            return@withState true
        }
    }

    override fun getLayoutId() = R.layout.fragment_viewer

    override fun getVglsFragmentTag() =
        this.javaClass.simpleName + ":${songId ?: viewerArgs.songId}"

    override fun getTrackingScreen() = TrackingScreen.SHEET_VIEWER

    override fun getDetails() =
        viewerArgs.songId?.toString() ?: viewerArgs.jamId?.toString() ?: ""

    override fun getPerfTrackingMinScreenHeight() = 200

    override fun getPerfSpec() = PerfSpec.VIEWER

    override fun configureStatusBarContentColor() {
        windowInsetController?.isAppearanceLightStatusBars = false
    }

    private fun startScreenTimer() {
        viewModel.startScreenTimer()
    }

    private fun stopScreenTimer() {
        viewModel.stopScreenTimer()
    }

    private fun setScreenOnLock() {
        val activity = activity ?: return
        Timber.v("Setting screen-on lock.")
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun clearScreenOnLock() {
        val activity = activity ?: return
        Timber.v("Clearing screen-on lock.")
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        screenOffSnack = showSnackbar(
            getString(R.string.snack_screen_off),
            onScreenOffSnackClick,
            R.string.cta_snack_screen_off,
            Snackbar.LENGTH_INDEFINITE
        )
    }

    private fun hideScreenOffSnackbar() {
        screenOffSnack?.dismiss()
        screenOffSnack = null
    }

    private fun showSong(song: Song?, selectedPart: Part) {
        if (song == null) {
            showEmptyState()
            return
        }

        hudViewModel.setSelectedSong(song)
        hudViewModel.setViewerScreenVisible()

        // Meaningless comment indicating a bugfix
        val pageCount = if (selectedPart == Part.VOCAL) {
            song.lyricPageCount
        } else {
            song.pageCount
        }

        val listComponents = (1..pageCount).map { pageNumber ->
            SheetListModel(
                Page.generateImageUrl(
                    baseImageUrl,
                    selectedPart,
                    song.filename,
                    pageNumber
                ),
                this
            )
        }

        sheetsAdapter.submitList(listComponents)
    }

    private fun showEmptyState() {
        showError("No sheet found.")
    }

    companion object {
        fun newInstance(sheetArgs: ViewerArgs): ViewerFragment {
            val fragment = ViewerFragment()

            val args = Bundle()
            args.putParcelable(Mavericks.KEY_ARG, sheetArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
