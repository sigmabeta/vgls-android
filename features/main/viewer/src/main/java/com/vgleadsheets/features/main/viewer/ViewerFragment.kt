package com.vgleadsheets.features.main.viewer

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.args
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.snackbar.Snackbar
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.ViewerArgs
import com.vgleadsheets.components.SheetListModel
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.model.pages.Page
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.tracking.TrackingScreen
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import timber.log.Timber

class ViewerFragment :
    VglsFragment(),
    SheetListModel.ImageListener {
    @Inject
    lateinit var viewerViewModelFactory: ViewerViewModel.Factory

    @Inject
    @Named("VglsImageUrl")
    lateinit var baseImageUrl: String

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: ViewerViewModel by fragmentViewModel()

    private val viewerArgs: ViewerArgs by args()

    // Keep a local copy for the fragment tag.
    private var songId: Long? = null

    private val sheetsAdapter = ComponentAdapter()

    private val timers = CompositeDisposable()

    private var screenOffSnack: Snackbar? = null

    private val onScreenOffSnackClick = View.OnClickListener {
        postInvalidate()
    }

    fun updateSongId(songId: Long) {
        viewModel.updateSongId(songId)
    }

    override fun onClicked() = withState(hudViewModel) { state ->
        if (state.hudVisible) {
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

        viewModel.selectSubscribe(
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

        viewModel.selectSubscribe(ViewerState::songId) {
            if (it != null) {
                viewModel.fetchSong()
            }
        }

        viewModel.selectSubscribe(ViewerState::jamCancellationReason) {
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
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, viewerState ->
        hudViewModel.alwaysShowBack()

        stopScreenTimer()
        hideScreenOffSnackbar()

        if (viewerState.screenOn is Success && viewerState.screenOn()?.value == true) {
            setScreenOnLock()
            startScreenTimer()
        }

        if (hudState.hudVisible && hudState.mode == HudMode.REGULAR) {
            hudViewModel.startHudTimer()
        }

        val selectedPart = hudState.selectedPart

        songId = viewerState.songId

        when (val song = viewerState.song) {
            is Fail -> showError(
                song.error.message ?: song.error::class.simpleName ?: "Unknown Error"
            )
            is Success -> showSong(viewerState.song(), selectedPart)
            Uninitialized -> Unit
            else -> {
                showError("No song found.")
            }
        }
    }

    override fun onBackPress() = withState(hudViewModel) { hudState ->
        if (hudState.hudVisible) {
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

    override fun getDetails() = viewerArgs.songId?.toString() ?: viewerArgs.jamId?.toString() ?: ""

    override fun getPerfTrackingMinScreenHeight() = 200

    override fun getPerfSpec() = PerfSpec.VIEWER

    private fun startScreenTimer() {
        Timber.v("Starting screen timer.")
        val screenTimer = Observable.timer(TIMEOUT_SCREEN_OFF_MINUTES, TimeUnit.MINUTES)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.v("Screen timer expired.")
                    clearScreenOnLock()
                    screenOffSnack = showSnackbar(
                        getString(R.string.snack_screen_off),
                        onScreenOffSnackClick,
                        R.string.cta_snack_screen_off,
                        Snackbar.LENGTH_INDEFINITE
                    )
                },
                {
                    showError(
                        "There was an error with the screen timer. Literally how" +
                            "did you make this happen?"
                    )
                }
            )

        timers.add(screenTimer)
    }

    private fun stopScreenTimer() {
        Timber.v("Clearing screen timer.")
        timers.clear()
    }

    private fun setScreenOnLock() {
        Timber.v("Setting screen-on lock.")
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun clearScreenOnLock() {
        Timber.v("Clearing screen-on lock.")
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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

        if (sheetsAdapter.currentList != listComponents) {
            sheetsAdapter.submitList(listComponents)
            Timber.w("Lists changed, submitting.")
        } else {
            Timber.i("Lists equivalent, not submitting.")
        }
    }

    private fun showEmptyState() {
        showError("No sheet found.")
    }

    companion object {
        const val TIMEOUT_SCREEN_OFF_MINUTES = 10L

        fun newInstance(sheetArgs: ViewerArgs): ViewerFragment {
            val fragment = ViewerFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, sheetArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
