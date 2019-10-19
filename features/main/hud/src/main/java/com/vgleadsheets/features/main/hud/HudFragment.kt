package com.vgleadsheets.features.main.hud

import android.animation.ObjectAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.REVERSE
import android.os.Bundle
import android.view.View
import android.view.View.SCALE_Y
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.vgleadsheets.Side
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.DECELERATE
import com.vgleadsheets.animation.DURATION_QUICK
import com.vgleadsheets.animation.DURATION_SLOW
import com.vgleadsheets.animation.TRANSLATION_CENTER
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInSlightly
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.slideViewDownOffscreen
import com.vgleadsheets.animation.slideViewOnscreen
import com.vgleadsheets.animation.slideViewUpOffscreen
import com.vgleadsheets.components.PartListModel
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForMargin
import com.vgleadsheets.setInsetListenerForOnePadding
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_hud.card_search
import kotlinx.android.synthetic.main.fragment_hud.edit_search_query
import kotlinx.android.synthetic.main.fragment_hud.shadow_hud
import kotlinx.android.synthetic.main.fragment_hud.text_search_hint
import kotlinx.android.synthetic.main.view_bottom_sheet_card.bottom_sheet
import kotlinx.android.synthetic.main.view_bottom_sheet_content.button_menu
import kotlinx.android.synthetic.main.view_bottom_sheet_content.icon_random
import kotlinx.android.synthetic.main.view_bottom_sheet_content.layout_all_sheets
import kotlinx.android.synthetic.main.view_bottom_sheet_content.layout_bottom_sheet
import kotlinx.android.synthetic.main.view_bottom_sheet_content.layout_by_composer
import kotlinx.android.synthetic.main.view_bottom_sheet_content.layout_by_game
import kotlinx.android.synthetic.main.view_bottom_sheet_content.layout_random_select
import kotlinx.android.synthetic.main.view_bottom_sheet_content.layout_refresh
import kotlinx.android.synthetic.main.view_bottom_sheet_content.list_parts
import kotlinx.android.synthetic.main.view_bottom_sheet_content.progress_hud
import kotlinx.android.synthetic.main.view_bottom_sheet_content.text_update_time
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.NoSuchElementException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("TooManyFunctions")
class HudFragment : VglsFragment(), PartListModel.ClickListener {
    @Inject
    lateinit var hudViewModelFactory: HudViewModel.Factory

    private val viewModel: HudViewModel by activityViewModel()

    private val disposables = CompositeDisposable()

    private val adapter = ComponentAdapter()

    private var randomAnimation: ObjectAnimator? = null

    override fun onClicked(clicked: PartListModel) {
        onPartSelect(clicked)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure search bar insets
        card_search.setInsetListenerForMargin(offset = resources.getDimension(R.dimen.margin_medium).toInt())

        val cornerOffset = resources.getDimension(R.dimen.margin_small).toInt()

        layout_bottom_sheet.setInsetListenerForOnePadding(Side.BOTTOM, offset = cornerOffset)
        bottom_sheet.updateLayoutParams<FrameLayout.LayoutParams> {
            bottomMargin = -cornerOffset
        }

        list_parts.adapter = adapter
        val gridLayoutManager = GridLayoutManager(activity, SPAN_COUNT_DEFAULT)
        list_parts.layoutManager = gridLayoutManager

        button_menu.setOnClickListener { onMenuClick() }
        shadow_hud.setOnClickListener { viewModel.onMenuAction() }

        layout_by_game.setOnClickListener {
            viewModel.onMenuAction()
            getFragmentRouter().showGameList()
        }
        layout_by_composer.setOnClickListener {
            viewModel.onMenuAction()
            getFragmentRouter().showComposerList()
        }
        layout_all_sheets.setOnClickListener {
            viewModel.onMenuAction()
            getFragmentRouter().showAllSheets()
        }
        layout_refresh.setOnClickListener {
            onRefreshClick()
        }

        enableRandomSelector()
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
                viewModel.searchClick()
            }
        disposables.add(clickEvents)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    @Suppress("ComplexMethod")
    override fun invalidate() = withState(viewModel) { state ->
        if (state.hudVisible) {
            showHud()
        } else {
            hideHud()
        }

        if (state.searchVisible) {
            showSearch()
        } else {
            hideSearch()
        }

        if (state.menuExpanded) {
            showFullMenu()
        } else {
            hideFullMenu()
        }

        when (state.random) {
            is Success -> {
                onRandomSuccess(state, state.random())
            }
            is Loading -> {
                startRandomLoadAnimation()
            }
            is Fail -> {
                showError("Failed to load random sheet.")
                viewModel.clearRandom()
            }
            is Uninitialized -> stopRandomLoadAnimation()
        }

        when (state.digest) {
            is Uninitialized -> hideDigestLoading()
            is Fail -> handleDigestError(state.digest.error)
            is Loading -> showDigestLoading()
            is Success -> {
                hideDigestLoading()
                viewModel.clearDigest()
            }
        }

        when (state.updateTime) {
            is Loading -> showUpdateTimeLoading()
            is Success -> showUpdateTimeSuccess(state.updateTime())
        }

        val listComponents = state.parts?.map {
            PartListModel(
                it.apiId.hashCode().toLong(),
                it.apiId,
                it.selected,
                this
            )
        }

        adapter.submitList(listComponents)
    }

    override fun getLayoutId() = R.layout.fragment_hud

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    override fun shouldTrackViews() = false

    private fun onRandomSuccess(
        hudState: HudState,
        song: Song?
    ) {
        viewModel.clearRandom()
        viewModel.onMenuAction()

        if (song == null) {
            showError("Failed to get a random track.")
            return
        }

        tracker.logRandomSongView(
            song.name,
            song.gameName,
            hudState.parts?.first { it.selected }?.apiId ?: "C"
        )

        getFragmentRouter().showSongViewer(song.id)
    }

    private fun onPartSelect(clicked: PartListModel) {
        tracker.logPartSelect(clicked.name)
        viewModel.onPartSelect(clicked.name)
    }

    private fun showUpdateTimeSuccess(updateTime: Long?) {
        val calendar = Calendar.getInstance()
        val checkedTime = updateTime ?: 0L

        val date = if (checkedTime > 0L) {
            calendar.timeInMillis = checkedTime
            val time = calendar.time
            val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
            dateFormat.format(time)
        } else {
            getString(R.string.date_never)
        }

        text_update_time.text = getString(R.string.label_refresh_date, date)
    }

    private fun onMenuClick() {
        tracker.logMenuShow()
        viewModel.onMenuClick()
    }

    private fun onRefreshClick() = withState(viewModel) {
        if (it.digest !is Loading) {
            tracker.logForceRefresh()
            viewModel.refresh()
        }
    }

    private fun showUpdateTimeLoading() {
        Timber.i("Loading update time.")
    }

    private fun showDigestLoading() {
        progress_hud.fadeIn()
    }

    private fun hideDigestLoading() {
        progress_hud.fadeOutGone()
    }

    private fun handleDigestError(error: Throwable) {
        when (error) {
            is NoSuchElementException -> hideDigestLoading()
            else -> {
                showError("Couldn't load sheets from server: ${error.message}")
                viewModel.clearDigest()
            }
        }
    }

    private fun showSearch() {
        viewModel.stopHudTimer()

        val imm = ContextCompat.getSystemService(activity!!, InputMethodManager::class.java)
        imm?.showSoftInput(edit_search_query, InputMethodManager.SHOW_IMPLICIT)

        text_search_hint.fadeOutGone()
        edit_search_query.fadeIn()
        edit_search_query.requestFocus()

        getFragmentRouter().showSearch()
    }

    private fun hideSearch() {
        // TODO Delay the text clearing
        edit_search_query.text.clear()
        edit_search_query.fadeOutGone()
        text_search_hint.fadeIn()

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
        if (card_search.visibility != View.GONE) {
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

    private fun showFullMenu() {
        if (layout_by_game.visibility != VISIBLE) {
            shadow_hud.fadeInSlightly()

            layout_by_game.fadeIn()
            layout_by_composer.fadeIn()
            layout_all_sheets.fadeIn()
            layout_random_select.fadeIn()
            layout_refresh.fadeIn()

            val itemHeight = resources.getDimension(R.dimen.min_clickable_size)
            val options = layout_bottom_sheet.childCount - CHILDREN_ABOVE_FOLD
            val slideDistance = (itemHeight * options)

            bottom_sheet.translationY = slideDistance
            bottom_sheet.animate()
                .translationY(TRANSLATION_CENTER)
                .setDuration(DURATION_SLOW)
                .setInterpolator(DECELERATE)
        }
    }

    private fun hideFullMenu() {
        if (layout_by_game.visibility == VISIBLE) {
            shadow_hud.fadeOutGone()

            layout_by_game.fadeOutGone()
            layout_by_composer.fadeOutGone()
            layout_all_sheets.fadeOutGone()
            layout_random_select.fadeOutGone()
            layout_refresh.fadeOutGone()

            val itemHeight = resources.getDimension(R.dimen.min_clickable_size)
            val options = layout_bottom_sheet.childCount - CHILDREN_ABOVE_FOLD
            val slideDistance = (itemHeight * options)

            bottom_sheet.animate()
                .translationY(slideDistance)
                .setDuration(DURATION_QUICK)
                .setInterpolator(DECELERATE)
                .withEndAction {
                    bottom_sheet.translationY = TRANSLATION_CENTER
                }
        }
    }

    private fun searchClicks() = card_search.clicks()
        .throttleFirst(THRESHOLD_SEARCH_CLICKS, TimeUnit.MILLISECONDS)

    private fun searchEvents() = edit_search_query
        .afterTextChangeEvents()
        .throttleLast(THRESHOLD_SEARCH_EVENTS, TimeUnit.MILLISECONDS)
        .map { it.editable.toString() }

    private fun startRandomLoadAnimation() {
        randomAnimation = ObjectAnimator.ofFloat(icon_random, SCALE_Y, 0.0f)

        randomAnimation?.repeatCount = INFINITE
        randomAnimation?.repeatMode = REVERSE

        randomAnimation?.start()

        disableRandomSelector()
    }

    private fun stopRandomLoadAnimation() {
        randomAnimation?.cancel()
        randomAnimation = null
        icon_random.scaleY = 1.0f

        enableRandomSelector()
    }

    private fun enableRandomSelector() {
        layout_random_select.setOnClickListener {
            viewModel.onRandomSelectClick()
        }
    }

    private fun disableRandomSelector() {
        layout_random_select.setOnClickListener(null)
    }

    companion object {
        const val THRESHOLD_SEARCH_EVENTS = 1500L
        const val THRESHOLD_SEARCH_CLICKS = 200L

        const val SPAN_COUNT_DEFAULT = 7

        const val CHILDREN_ABOVE_FOLD = 2

        fun newInstance() = HudFragment()
    }
}
