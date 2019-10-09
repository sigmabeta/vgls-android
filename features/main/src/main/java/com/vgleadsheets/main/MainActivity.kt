package com.vgleadsheets.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.FragmentTransaction
import com.airbnb.mvrx.BaseMvRxActivity
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.args.SongArgs
import com.vgleadsheets.features.main.composer.ComposerFragment
import com.vgleadsheets.features.main.composer.ComposerListFragment
import com.vgleadsheets.features.main.game.GameFragment
import com.vgleadsheets.features.main.games.GameListFragment
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.search.SearchFragment
import com.vgleadsheets.features.main.songs.SongListFragment
import com.vgleadsheets.features.main.viewer.ViewerFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.toplevel
import javax.inject.Inject

@Suppress("TooManyFunctions")
class MainActivity : BaseMvRxActivity(), HasAndroidInjector, FragmentRouter,
    HudViewModel.HudContainer {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = androidInjector

    override fun getHudFragment() =
        supportFragmentManager.findFragmentById(R.id.frame_hud) as HudFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.VglsImmersive)
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
        // TODO Move to Navigator Fragment
        supportFragmentManager.beginTransaction()
            .setDefaultAnimations()
            .replace(R.id.frame_fragment, GameListFragment.newInstance())
            .commit()
    }

    override fun showComposerList() {
        // TODO Clear back stack before doing this.
        // TODO Move to Navigator Fragment
        supportFragmentManager.beginTransaction()
            .setDefaultAnimations()
            .replace(R.id.frame_fragment, ComposerListFragment.newInstance())
            .commit()
    }

    override fun showAllSheets() {
        // TODO Clear back stack before doing this.
        // TODO Move to Navigator Fragment
        supportFragmentManager.beginTransaction()
            .setDefaultAnimations()
            .replace(R.id.frame_fragment, SongListFragment.newInstance())
            .commit()
    }

    override fun showRandomSheet() {
        Toast.makeText(this, "Unimplemented.", LENGTH_SHORT).show()
    }

    override fun showSongListForGame(gameId: Long) = showFragmentSimple(
        GameFragment.newInstance(
            IdArgs(gameId)
        )
    )

    override fun showSongListForComposer(composerId: Long) = showFragmentSimple(
        ComposerFragment.newInstance(
            IdArgs(composerId)
        )
    )

    override fun showSongViewer(songId: Long) = showFragmentSimple(
        ViewerFragment.newInstance(
            SongArgs(
                songId
            )
        )
    )

    override fun onBackPressed() {
        if (!getDisplayedFragment().onBackPress()) {
            super.onBackPressed()
        }
    }

    private fun showFragmentSimple(fragment: VglsFragment) {
        if (getDisplayedFragment().getVglsFragmentTag() != fragment.getVglsFragmentTag()) {
            supportFragmentManager.beginTransaction()
                .setDefaultAnimations()
                .replace(R.id.frame_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun getDisplayedFragment() =
        supportFragmentManager.findFragmentById(R.id.frame_fragment) as VglsFragment

    private fun addHud() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_hud, HudFragment.newInstance())
            .commit()
    }

    private fun FragmentTransaction.setDefaultAnimations() = setCustomAnimations(
        R.anim.enter,
        R.anim.exit,
        R.anim.enter_pop,
        R.anim.exit_pop
    )
}
