package com.vgleadsheets.features.main.hud

import android.os.Bundle
import android.view.View
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
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForMargin
import com.vgleadsheets.setInsetListenerForOnePadding
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_hud.*
import kotlinx.android.synthetic.main.view_bottom_sheet_card.*
import kotlinx.android.synthetic.main.view_bottom_sheet_content.*
import java.util.concurrent.TimeUnit

@Suppress("TooManyFunctions")
class HudFragment : VglsFragment(), PartListModel.ClickListener {
    private val viewModel: HudViewModel by activityViewModel()

    private val disposables = CompositeDisposable()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: PartListModel) {
        viewModel.onPartSelect(clicked.name)
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

        button_menu.setOnClickListener { viewModel.onMenuClick() }
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
        layout_random_select.setOnClickListener {
            viewModel.onMenuAction()
            getFragmentRouter().showRandomSheet()
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
                viewModel.searchClick()
            }
        disposables.add(clickEvents)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

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

    companion object {
        const val THRESHOLD_SEARCH_EVENTS = 1500L
        const val THRESHOLD_SEARCH_CLICKS = 200L

        const val SPAN_COUNT_DEFAULT = 7

        const val CHILDREN_ABOVE_FOLD = 1

        fun newInstance() = HudFragment()
    }
}
