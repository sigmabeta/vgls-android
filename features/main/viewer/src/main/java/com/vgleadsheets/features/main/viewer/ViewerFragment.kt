package com.vgleadsheets.features.main.viewer

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.args
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.snackbar.Snackbar
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.slideViewOnscreen
import com.vgleadsheets.animation.slideViewUpOffscreen
import com.vgleadsheets.args.ViewerArgs
import com.vgleadsheets.components.SheetListModel
import com.vgleadsheets.components.ToolbarItemListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.getYoutubeSearchUrlForQuery
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import com.vgleadsheets.tracking.TrackingScreen
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_viewer.list_sheets
import kotlinx.android.synthetic.main.fragment_viewer.list_toolbar_items
import kotlinx.android.synthetic.main.fragment_viewer.pager_sheets
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("TooManyFunctions")
class ViewerFragment : VglsFragment(),
    SheetListModel.ImageListener,
    ToolbarItemListModel.EventHandler {
    @Inject
    lateinit var viewerViewModelFactory: ViewerViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: ViewerViewModel by fragmentViewModel()

    private val viewerArgs: ViewerArgs by args()

    // Keep a local copy for the fragment tag.
    private var songId: Long? = null

    private val sheetsAdapter = ComponentAdapter()

    private val toolbarAdapter = ComponentAdapter()

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

    override fun onClicked(clicked: ToolbarItemListModel) = when (clicked.iconId) {
        R.drawable.ic_details_24 -> showSheetDetails()
        R.drawable.ic_play_circle_filled_24 -> showYoutubeSearch()
        else -> showError("Unimplemented toolbar button.")
    }

    override fun onLoadStarted() {
        perfTracker.onTitleLoaded(getPerfScreenName())
    }

    override fun onLoadComplete() {
        perfTracker.onTransitionStarted(getPerfScreenName())
        perfTracker.onPartialContentLoad(getPerfScreenName())
        perfTracker.onFullContentLoad(getPerfScreenName())
    }

    override fun onLongClicked(clicked: ToolbarItemListModel) {
        hudViewModel.startHudTimer()
        showSnackbar(clicked.name)
    }

    override fun clearClicked() = Unit

    override fun onLoadFailed(imageUrl: String, ex: Exception?) {
        perfTracker.cancel(getPerfScreenName())
        showError("Image load failed: ${ex?.message ?: "Unknown Error"}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pager_sheets?.adapter = sheetsAdapter

        list_sheets?.adapter = sheetsAdapter
        list_sheets?.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        val topOffset = resources.getDimension(R.dimen.margin_xlarge).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()
        val sideOffset = resources.getDimension(R.dimen.margin_medium).toInt()

        list_toolbar_items?.setInsetListenerForPadding(
            topOffset = topOffset,
            leftOffset = sideOffset,
            rightOffset = sideOffset
        )

        list_toolbar_items?.adapter = toolbarAdapter
        list_toolbar_items?.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            true
        )

        setUpToolbarItems()

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

        hudViewModel.resetAvailableParts()
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, viewerState ->
        hudViewModel.alwaysShowBack()

        stopScreenTimer()
        hideScreenOffSnackbar()

        if (viewerState.screenOn is Success && viewerState.screenOn()?.value == true) {
            setScreenOnLock()
            startScreenTimer()
        }

        if (hudState.hudVisible && !hudState.searchVisible) {
            hudViewModel.startHudTimer()
        }

        if (hudState.hudVisible) {
            list_toolbar_items?.slideViewOnscreen()
        } else {
            list_toolbar_items?.slideViewUpOffscreen()
        }

        val selectedPart = hudState.parts.first { it.selected }

        songId = viewerState.songId

        when (viewerState.song) {
            is Fail -> showError(
                viewerState.song.error.message
                    ?: viewerState.song.error::class.simpleName ?: "Unknown Error"
            )
            is Success -> showSheet(viewerState.song(), selectedPart)
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

    private fun setUpToolbarItems() {
        val toolbarItems = listOf(
            ToolbarItemListModel(
                getString(R.string.menu_item_label_sheet_detail),
                R.drawable.ic_details_24,
                this
            ),
            ToolbarItemListModel(
                getString(R.string.menu_item_label_youtube),
                R.drawable.ic_play_circle_filled_24,
                this
            )
        )

        toolbarAdapter.submitList(toolbarItems)
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

    private fun showSheet(sheet: Song?, partSelection: PartSelectorItem) {
        if (sheet == null) {
            showEmptyState()
            return
        }

        val parts = sheet.parts
        if (parts != null) {
            hudViewModel.setAvailableParts(parts)
        } else {
            showError("Unable to determine which parts are available for this sheet.")
        }

        val selectedPart = sheet.parts?.firstOrNull { it.name == partSelection.apiId }

        if (selectedPart == null) {
            showError("This sheet doesn't include the part you selected. Choose another.")
            return
        }

        val listComponents = selectedPart.pages?.map {
            SheetListModel(
                it.imageUrl,
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

    private fun showSheetDetails() = withState(viewModel) { state ->
        val songId = state.songId
        if (songId != null) {
            getFragmentRouter().showSheetDetail(songId)
        }
    }

    private fun showYoutubeSearch() = withState(viewModel) {
        if (it.song is Success) {
            val song = it.song()!!
            val query = "${song.gameName} - ${song.name}"

            val youtubeUrl = getYoutubeSearchUrlForQuery(query)

            getFragmentRouter().goToWebUrl(youtubeUrl)
        }
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
