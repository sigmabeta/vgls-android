package com.vgleadsheets.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.BaseMvRxActivity
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.args.SongArgs
import com.vgleadsheets.features.main.games.GameListFragment
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.search.SearchFragment
import com.vgleadsheets.features.main.songs.SongListFragment
import com.vgleadsheets.features.main.viewer.ViewerFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
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


        if (savedInstanceState == null) {
            addHud()
            showGameList()
        }
    }

    override fun showSearch() {
        showFragmentSimple(SearchFragment.newInstance())
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

    override fun onBackPressed() {
        getDisplayedFragment().onBackPress()
        super.onBackPressed()
    }

    private fun showFragmentSimple(fragment: VglsFragment) {
        if (getDisplayedFragment().getVglsFragmentTag() != fragment.getVglsFragmentTag()) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun getDisplayedFragment() = supportFragmentManager.findFragmentById(R.id.frame_fragment) as VglsFragment

    private fun addHud() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_hud, HudFragment.newInstance())
            .commit()
    }
}
