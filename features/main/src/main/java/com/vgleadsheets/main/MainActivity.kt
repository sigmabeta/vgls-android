package com.vgleadsheets.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.airbnb.mvrx.BaseMvRxActivity
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.args.SongArgs
import com.vgleadsheets.features.main.about.AboutFragment
import com.vgleadsheets.features.main.composer.ComposerFragment
import com.vgleadsheets.features.main.composers.ComposerListFragment
import com.vgleadsheets.features.main.game.GameFragment
import com.vgleadsheets.features.main.games.GameListFragment
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.license.LicenseFragment
import com.vgleadsheets.features.main.search.SearchFragment
import com.vgleadsheets.features.main.settings.SettingsFragment
import com.vgleadsheets.features.main.songs.SongListFragment
import com.vgleadsheets.features.main.tag_key.TagKeyFragment
import com.vgleadsheets.features.main.viewer.ViewerFragment
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.toplevel
import javax.inject.Inject

@Suppress("TooManyFunctions")
class MainActivity : BaseMvRxActivity(), HasAndroidInjector, FragmentRouter,
    HudViewModel.HudViewModelFactoryProvider {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    override lateinit var hudViewModelFactory: HudViewModel.Factory

    override fun androidInjector() = androidInjector

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
        }
    }

    override fun showSearch() {
        showFragmentSimple(SearchFragment.newInstance(), false)
    }

    override fun showGameList() {
        clearBackStack()
        // TODO Move to Navigator Fragment
        supportFragmentManager.beginTransaction()
            .setDefaultAnimations()
            .replace(R.id.frame_fragment, GameListFragment.newInstance())
            .commit()
    }

    override fun showComposerList() {
        clearBackStack()
        // TODO Move to Navigator Fragment
        supportFragmentManager.beginTransaction()
            .setDefaultAnimations()
            .replace(R.id.frame_fragment, ComposerListFragment.newInstance())
            .commit()
    }

    override fun showTagList() {
        clearBackStack()
        // TODO Move to Navigator Fragment
        supportFragmentManager.beginTransaction()
            .setDefaultAnimations()
            .replace(R.id.frame_fragment, TagKeyFragment.newInstance())
            .commit()
    }

    override fun showAllSheets() {
        clearBackStack()
        // TODO Move to Navigator Fragment
        supportFragmentManager.beginTransaction()
            .setDefaultAnimations()
            .replace(R.id.frame_fragment, SongListFragment.newInstance())
            .commit()
    }

    override fun showSettings() {
        showFragmentSimple(SettingsFragment.newInstance())
    }

    override fun showAbout() {
        showFragmentSimple(AboutFragment.newInstance())
    }

    override fun goToWebUrl(url: String) {
        val launcher = Intent(Intent.ACTION_VIEW)
        launcher.data = Uri.parse(url)
        startActivity(launcher)
    }

    override fun showLicenseScreen() {
        showFragmentSimple(LicenseFragment.newInstance())
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
        if (!getHudFragment().onBackPress() && !getDisplayedFragment().onBackPress()) {
            super.onBackPressed()
        }
    }

    override fun onScreenSwitch() {
        getHudFragment().onScreenSwitch()
    }

    private fun clearBackStack() {
        while (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun showFragmentSimple(fragment: VglsFragment, blockSearchFocus: Boolean = true) {
        if (getDisplayedFragment().getVglsFragmentTag() != fragment.getVglsFragmentTag()) {
            supportFragmentManager.beginTransaction()
                .setDefaultAnimations()
                .replace(R.id.frame_fragment, fragment)
                .addToBackStack(null)
                .commit()

            // TODO Oofsville, this is a hack for the ages.
            if (blockSearchFocus && doesDeviceHaveFocusGlitch()) {
                getHudFragment().onScreenSwitch()
            }
        }
    }

    private fun doesDeviceHaveFocusGlitch() = Build.VERSION.SDK_INT < Build.VERSION_CODES.P

    private fun getHudFragment() =
        supportFragmentManager.findFragmentById(R.id.frame_hud) as HudFragment

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
