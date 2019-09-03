package com.vgleadsheets.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.BaseMvRxActivity
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.args.SongArgs
import com.vgleadsheets.features.main.games.GameListFragment
import com.vgleadsheets.features.main.search.SearchFragment
import com.vgleadsheets.features.main.songs.SongListFragment
import com.vgleadsheets.features.main.viewer.ViewerFragment
import com.vgleadsheets.setInsetListenerForMargin
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("TooManyFunctions")
class MainActivity : BaseMvRxActivity(), HasSupportFragmentInjector, FragmentRouter {

    @Inject lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingFragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Configure app for edge-to-edge
        toplevel.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        // Configure search bar insets
        card_search.setInsetListenerForMargin(offset = resources.getDimension(R.dimen.margin_medium).toInt())

        if (savedInstanceState == null) {
            showGameList()
        }
    }

    override fun showGameList() {
        // TODO Clear back stack before doing this.
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_fragment, GameListFragment.newInstance())
            .commit()
    }

    override fun showSongListForGame(gameId: Long) = showFragmentSimple(SongListFragment.newInstance(
        IdArgs(
            gameId
        )
    ))

    override fun showSongViewer(songId: Long) = showFragmentSimple(ViewerFragment.newInstance(
        SongArgs(
            songId
        )
    ))

    override fun showSearch() {
        text_search_hint.fadeOutGone()
        edit_search_query.fadeIn()
        edit_search_query.requestFocus()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(edit_search_query, SHOW_IMPLICIT)

        showFragmentSimple(SearchFragment.newInstance())
    }

    override fun searchClicks() = card_search.clicks()
        .throttleFirst(THRESHOLD_SEARCH_CLICKS, TimeUnit.MILLISECONDS)

    override fun searchEvents() = edit_search_query
        .afterTextChangeEvents()
        .throttleLast(THRESHOLD_SEARCH_EVENTS, TimeUnit.MILLISECONDS)
        .map { it.editable.toString() }

    override fun hideSearch() {
        edit_search_query.fadeOutGone()
        text_search_hint.fadeIn()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edit_search_query.windowToken, HIDE_IMPLICIT_ONLY)
    }

    private fun showFragmentSimple(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun getDisplayedFragment() = supportFragmentManager.findFragmentById(R.id.frame_fragment) as VglsFragment

    companion object {
        const val THRESHOLD_SEARCH_EVENTS = 1500L
        const val THRESHOLD_SEARCH_CLICKS = 200L
    }
}
