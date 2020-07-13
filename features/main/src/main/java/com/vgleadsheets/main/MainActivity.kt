package com.vgleadsheets.main

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.airbnb.mvrx.BaseMvRxActivity
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.args.ViewerArgs
import com.vgleadsheets.features.main.about.AboutFragment
import com.vgleadsheets.features.main.composer.ComposerFragment
import com.vgleadsheets.features.main.composers.ComposerListFragment
import com.vgleadsheets.features.main.debug.DebugFragment
import com.vgleadsheets.features.main.game.GameFragment
import com.vgleadsheets.features.main.games.GameListFragment
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.jam.JamFragment
import com.vgleadsheets.features.main.jams.FindJamDialogFragment
import com.vgleadsheets.features.main.jams.JamListFragment
import com.vgleadsheets.features.main.license.LicenseFragment
import com.vgleadsheets.features.main.search.SearchFragment
import com.vgleadsheets.features.main.settings.SettingsFragment
import com.vgleadsheets.features.main.sheet.SheetDetailFragment
import com.vgleadsheets.features.main.songs.SongListFragment
import com.vgleadsheets.features.main.tagkeys.TagKeyFragment
import com.vgleadsheets.features.main.tagsongs.TagValueSongListFragment
import com.vgleadsheets.features.main.tagvalues.TagValueListFragment
import com.vgleadsheets.features.main.viewer.ViewerFragment
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.TrackingScreen
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

    @Inject
    lateinit var tracker: Tracker

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
        showFragmentSimple(
            SearchFragment.newInstance()
        )
    }

    override fun showGameList() {
        showTopLevelFragment(
            GameListFragment.newInstance()
        )
    }

    override fun showComposerList() {
        showTopLevelFragment(
            ComposerListFragment.newInstance()
        )
    }

    override fun showTagList() {
        showTopLevelFragment(
            TagKeyFragment.newInstance()
        )
    }

    override fun showJams() {
        showTopLevelFragment(
            JamListFragment.newInstance()
        )
    }

    override fun showAllSheets() {
        showTopLevelFragment(
            SongListFragment.newInstance()
        )
    }

    override fun showSettings() {
        showFragmentSimple(
            SettingsFragment.newInstance()
        )
    }

    override fun showDebug() {
        showFragmentSimple(
            DebugFragment.newInstance()
        )
    }

    override fun showAbout() {
        showFragmentSimple(
            AboutFragment.newInstance()
        )
    }

    override fun goToWebUrl(url: String) {
        val launcher = Intent(Intent.ACTION_VIEW)
        launcher.data = Uri.parse(url)
        startActivity(launcher)

        val displayedFragment = getDisplayedFragment() ?: throw IllegalStateException(
            "How are we launching a web brwoser from a blank view?"
        )

        tracker.logWebLaunch(
            url,
            displayedFragment.getTrackingScreen(),
            displayedFragment.getArgs()?.id?.toString() ?: ""
        )
    }

    override fun showLicenseScreen() = showFragmentSimple(
        LicenseFragment.newInstance()
    )

    override fun showFindJamDialog() = FindJamDialogFragment
        .newInstance()
        .show(
            supportFragmentManager, FindJamDialogFragment::class.java.simpleName
        )

    override fun showSongListForGame(gameId: Long) = showFragmentSimple(
        GameFragment.newInstance(IdArgs(gameId))
    )

    override fun showSongListForComposer(composerId: Long) = showFragmentSimple(
        ComposerFragment.newInstance(IdArgs(composerId))
    )

    override fun showValueListForTagKey(tagKeyId: Long) = showFragmentSimple(
        TagValueListFragment.newInstance(IdArgs(tagKeyId))
    )

    override fun showSongListForTagValue(tagValueId: Long) = showFragmentSimple(
        TagValueSongListFragment.newInstance(IdArgs(tagValueId))
    )

    override fun showSheetDetail(songId: Long) = showFragmentSimple(
        SheetDetailFragment.newInstance(IdArgs(songId))
    )

    override fun showSongViewer(songId: Long) = showFragmentSimple(
        ViewerFragment.newInstance(ViewerArgs(songId = songId))
    )


    override fun showJamViewer(jamId: Long) = showFragmentSimple(
        ViewerFragment.newInstance(ViewerArgs(jamId = jamId))
    )


    override fun showJamDetailViewer(jamId: Long) = showFragmentSimple(
        JamFragment.newInstance(IdArgs(jamId))
    )

    override fun onBackPressed() {
        if (!getHudFragment().onBackPress() && getDisplayedFragment()?.onBackPress() != true) {
            super.onBackPressed()
        }
    }

    override fun restartApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        finish()
        Runtime.getRuntime().exit(0)
    }

    private fun clearBackStack() {
        while (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun showFragmentSimple(fragment: VglsFragment) {
        val displayedFragment = getDisplayedFragment()

        if (displayedFragment?.getVglsFragmentTag() != fragment.getVglsFragmentTag()) {
            tracker.logScreenView(
                this,
                fragment.getTrackingScreen(),
                fragment.getArgs()?.id?.toString() ?: "",
                displayedFragment?.getTrackingScreen() ?: TrackingScreen.NONE,
                displayedFragment?.getArgs()?.id?.toString() ?: ""
            )

            supportFragmentManager.beginTransaction()
                .setDefaultAnimations()
                .replace(R.id.frame_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showTopLevelFragment(fragment: VglsFragment) {
        clearBackStack()

        val displayedFragment = getDisplayedFragment()

        tracker.logScreenView(
            this,
            fragment.getTrackingScreen(),
            fragment.getArgs()?.id?.toString() ?: "",
            displayedFragment?.getTrackingScreen() ?: TrackingScreen.NONE,
            displayedFragment?.getArgs()?.id?.toString() ?: ""
        )

        supportFragmentManager.beginTransaction()
            .setDefaultAnimations()
            .replace(R.id.frame_fragment, fragment)
            .commit()
    }

    private fun getHudFragment() =
        supportFragmentManager.findFragmentById(R.id.frame_hud) as HudFragment

    private fun getDisplayedFragment() =
        supportFragmentManager.findFragmentById(R.id.frame_fragment) as VglsFragment?

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
