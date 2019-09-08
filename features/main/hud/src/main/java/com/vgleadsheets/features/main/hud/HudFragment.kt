package com.vgleadsheets.features.main.hud

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.setInsetListenerForMargin
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_hud.*
import java.util.concurrent.TimeUnit

class HudFragment : VglsFragment() {
    private val viewModel: HudViewModel by activityViewModel()

    private val disposables = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure search bar insets
        card_search.setInsetListenerForMargin(offset = resources.getDimension(R.dimen.margin_medium).toInt())
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

    override fun getLayoutId() = R.layout.fragment_hud

    override fun invalidate() = withState(viewModel) {
        if (it.searchVisible) {
            showSearch()
        } else {
            hideSearch()
        }

    }

    private fun showSearch() {
        val imm = ContextCompat.getSystemService(activity!!, InputMethodManager::class.java)
        imm?.showSoftInput(edit_search_query, InputMethodManager.SHOW_IMPLICIT)

        text_search_hint.fadeOutGone()
        edit_search_query.fadeIn()
        edit_search_query.requestFocus()

        getFragmentRouter().showSearch()
    }

    private fun hideSearch() {
        edit_search_query.fadeOutGone()
        text_search_hint.fadeIn()

        val imm = ContextCompat.getSystemService(activity!!, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(edit_search_query.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
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

        fun newInstance() = HudFragment()
    }
}
