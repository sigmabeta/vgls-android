package com.vgleadsheets.features.main.hud

import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
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
import com.vgleadsheets.features.main.hud.parts.PartAdapter
import com.vgleadsheets.setInsetListenerForMargin
import com.vgleadsheets.setInsetListenerForOnePadding
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_hud.*
import java.util.concurrent.TimeUnit

@Suppress("TooManyFunctions")
class HudFragment : VglsFragment() {
    private val viewModel: HudViewModel by activityViewModel()

    private val disposables = CompositeDisposable()

    private val adapter = PartAdapter(this)

    fun onItemClick(apiId: String) {
        viewModel.onPartSelect(apiId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure search bar insets
        card_search.setInsetListenerForMargin(offset = resources.getDimension(R.dimen.margin_medium).toInt())

        motion_bottom_sheet.setInsetListenerForOnePadding(Side.BOTTOM)

        list_parts.adapter = adapter
        list_parts.layoutManager = GridLayoutManager(activity, SPAN_COUNT_DEFAULT)

        button_menu.setOnClickListener { viewModel.onMenuClick() }
        layout_by_game.setOnClickListener { getFragmentRouter().showGameList() }
        layout_by_composer.setOnClickListener { getFragmentRouter().showComposerList() }
        layout_all_sheets.setOnClickListener { getFragmentRouter().showAllSheets() }
        layout_random_select.setOnClickListener { getFragmentRouter().showRandomSheet() }
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
        adapter.dataset = state.parts
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
        list_parts.animate().cancel()

        card_search.slideViewOnscreen()
        list_parts.slideViewOnscreen()

        view?.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    private fun hideHud() {
        if (card_search.visibility != View.GONE) {
            card_search.slideViewUpOffscreen()
            list_parts.slideViewDownOffscreen()

            view?.systemUiVisibility = SYSTEM_UI_FLAG_IMMERSIVE or
                    SYSTEM_UI_FLAG_FULLSCREEN or
                    SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    private fun showFullMenu() {
        motion_bottom_sheet.transitionToEnd()
    }

    private fun hideFullMenu() {
        motion_bottom_sheet.transitionToStart()
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

        fun newInstance() = HudFragment()
    }
}
