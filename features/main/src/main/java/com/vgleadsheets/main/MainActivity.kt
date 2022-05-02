package com.vgleadsheets.main

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentTransaction
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
import javax.inject.Inject
import timber.log.Timber

@Suppress("TooManyFunctions", "Deprecation")
class MainActivity :
    AppCompatActivity(),
    HasAndroidInjector,
    FragmentRouter,
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
        val toplevel = findViewById<CoordinatorLayout>(R.id.toplevel)
        toplevel.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        val displayMetrics = resources.displayMetrics
        val widthPixels = displayMetrics.widthPixels
        val heightPixels = displayMetrics.heightPixels

        Timber.v("Device screen DPI: ${displayMetrics.densityDpi}")
        Timber.v("Device screen scaling factor: ${displayMetrics.density}")
        Timber.v("Device screen size: ${widthPixels}x$heightPixels")
        Timber.v(
            "Device screen size (scaled): ${(widthPixels / displayMetrics.density).toInt()}" +
                "x${(heightPixels / displayMetrics.density).toInt()}"
        )

        if (savedInstanceState == null) {
            addHud()
        }
    }

    override fun showSearch() {
        showFragmentSimple(
            SearchFragment.newInstance()
        )
    }

    override fun showGameList(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showTopLevelFragment(
            GameListFragment.newInstance(),
            fromScreen,
            fromDetails
        )
    }

    override fun showComposerList(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showTopLevelFragment(
            ComposerListFragment.newInstance(),
            fromScreen,
            fromDetails
        )
    }

    override fun showTagList(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showTopLevelFragment(
            TagKeyFragment.newInstance(),
            fromScreen,
            fromDetails
        )
    }

    override fun showJams(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showTopLevelFragment(
            JamListFragment.newInstance(),
            fromScreen,
            fromDetails
        )
    }

    override fun showAllSheets(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showTopLevelFragment(
            SongListFragment.newInstance(),
            fromScreen,
            fromDetails
        )
    }

    override fun showSettings(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showFragmentSimple(
            SettingsFragment.newInstance(),
            fromScreen,
            fromDetails
        )
    }

    override fun showDebug(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showFragmentSimple(
            DebugFragment.newInstance(),
            fromScreen,
            fromDetails
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
            "How are we launching a web browser from a blank view?"
        )

        tracker.logWebLaunch(
            url,
            displayedFragment.getTrackingScreen(),
            displayedFragment.getDetails()
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

    override fun showSongListForGame(gameId: Long, name: String) {
        val prevFragment = getDisplayedFragment()

        tracker.logGameView(
            name,
            prevFragment?.getTrackingScreen() ?: TrackingScreen.NONE,
            prevFragment?.getDetails() ?: ""
        )

        showFragmentSimple(
            GameFragment.newInstance(IdArgs(gameId))
        )
    }

    override fun showSongListForComposer(composerId: Long, name: String) {
        val prevFragment = getDisplayedFragment()

        tracker.logComposerView(
            name,
            prevFragment?.getTrackingScreen() ?: TrackingScreen.NONE,
            prevFragment?.getDetails() ?: ""
        )

        showFragmentSimple(
            ComposerFragment.newInstance(IdArgs(composerId))
        )
    }

    override fun showValueListForTagKey(tagKeyId: Long) = showFragmentSimple(
        TagValueListFragment.newInstance(IdArgs(tagKeyId))
    )

    override fun showSongListForTagValue(tagValueId: Long) = showFragmentSimple(
        TagValueSongListFragment.newInstance(IdArgs(tagValueId))
    )

    override fun showSheetDetail(songId: Long) = showFragmentSimple(
        SheetDetailFragment.newInstance(IdArgs(songId))
    )

    override fun showSongViewer(
        songId: Long,
        name: String,
        gameName: String,
        transposition: String,
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        val prevFragment = getDisplayedFragment()

        tracker.logSongView(
            songId,
            name,
            gameName,
            transposition,
            fromScreen ?: prevFragment?.getTrackingScreen() ?: TrackingScreen.NONE,
            fromDetails ?: prevFragment?.getDetails() ?: ""
        )

        showFragmentSimple(
            ViewerFragment.newInstance(ViewerArgs(songId = songId)),
            fromScreen,
            fromDetails
        )
    }

    override fun showJamViewer(jamId: Long) {
        val prevFragment = getDisplayedFragment()

        tracker.logJamFollow(
            jamId,
            prevFragment?.getTrackingScreen() ?: TrackingScreen.NONE,
            prevFragment?.getDetails() ?: ""
        )

        showFragmentSimple(
            ViewerFragment.newInstance(ViewerArgs(jamId = jamId))
        )
    }

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

    private fun showFragmentSimple(
        fragment: VglsFragment,
        fromScreen: TrackingScreen? = null,
        fromDetails: String? = null
    ) {
        val displayedFragment = getDisplayedFragment()

        if (displayedFragment?.getVglsFragmentTag() != fragment.getVglsFragmentTag()) {
            tracker.logScreenView(
                this,
                fragment.getTrackingScreen(),
                fragment.getDetails(),
                fromScreen ?: displayedFragment?.getTrackingScreen() ?: TrackingScreen.NONE,
                fromDetails ?: displayedFragment?.getDetails() ?: ""
            )

            supportFragmentManager.beginTransaction()
                .setDefaultAnimations()
                .replace(R.id.frame_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showTopLevelFragment(
        fragment: VglsFragment,
        fromScreen: TrackingScreen? = null,
        fromDetails: String? = null
    ) {
        clearBackStack()

        val displayedFragment = getDisplayedFragment()

        tracker.logScreenView(
            this,
            fragment.getTrackingScreen(),
            fragment.getDetails(),
            fromScreen ?: displayedFragment?.getTrackingScreen() ?: TrackingScreen.NONE,
            fromDetails ?: displayedFragment?.getDetails() ?: ""
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
