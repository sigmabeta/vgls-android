package com.vgleadsheets.main

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentTransaction
import androidx.metrics.performance.FrameData
import androidx.metrics.performance.JankStats
import androidx.metrics.performance.PerformanceMetricsState
import com.vgleadsheets.FragmentInterface
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.args.NullableStringArgs
import com.vgleadsheets.args.ViewerArgs
import com.vgleadsheets.features.main.about.AboutFragment
import com.vgleadsheets.features.main.composer.ComposerDetailFragment
import com.vgleadsheets.features.main.composers.ComposerListFragment
import com.vgleadsheets.features.main.debug.DebugFragment
import com.vgleadsheets.features.main.favorites.FavoriteListFragment
import com.vgleadsheets.features.main.game.GameFragment
import com.vgleadsheets.features.main.games.GameListFragment
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.license.LicenseFragment
import com.vgleadsheets.features.main.search.SearchFragment
import com.vgleadsheets.features.main.settings.SettingFragment
import com.vgleadsheets.features.main.sheet.SongFragment
import com.vgleadsheets.features.main.songs.SongListFragment
import com.vgleadsheets.features.main.tagkeys.TagKeyListFragment
import com.vgleadsheets.features.main.tagsongs.TagValueSongFragment
import com.vgleadsheets.features.main.tagvalues.TagValueFragment
import com.vgleadsheets.features.main.viewer.ViewerFragment
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.perf.tracking.common.FrameInfo
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.tracking.TrackingScreen
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

@Suppress("TooManyFunctions", "Deprecation")
class MainActivity :
    AppCompatActivity(),
    HasAndroidInjector,
    FragmentRouter,
    FragmentInterface,
    HudViewModel.HudViewModelFactoryProvider {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    override lateinit var hudViewModelFactory: HudViewModel.Factory

    @Inject
    lateinit var perfTracker: PerfTracker

    @Inject
    lateinit var hatchet: Hatchet

    private var jankStats: JankStats? = null

    private var metricsStateHolder: PerformanceMetricsState.Holder? = null

    override fun androidInjector() = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(com.vgleadsheets.ui_core.R.style.VglsAppTheme)
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initializeJankStats()
        setupEdgeToEdge()
        printDisplayDetails()

        if (savedInstanceState == null) {
            addHud()
        }
    }

    private fun setupEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun printDisplayDetails() {
        val displayMetrics = resources.displayMetrics
        val widthPixels = displayMetrics.widthPixels
        val heightPixels = displayMetrics.heightPixels

        hatchet.v(this.javaClass.simpleName, "Device screen DPI: ${displayMetrics.densityDpi}")
        hatchet.v(
            this.javaClass.simpleName,
            "Device screen scaling factor: ${displayMetrics.density}"
        )
        hatchet.v(this.javaClass.simpleName, "Device screen size: ${widthPixels}x$heightPixels")
        hatchet.v(
            this.javaClass.simpleName,
            "Device screen size (scaled): ${(widthPixels / displayMetrics.density).toInt()}" +
                "x${(heightPixels / displayMetrics.density).toInt()}"
        )
    }

    override fun onResume() {
        super.onResume()
        jankStats?.isTrackingEnabled = true
    }

    override fun onPause() {
        super.onPause()
        jankStats?.isTrackingEnabled = false
    }

    override fun setPerfSpec(specName: String) {
        metricsStateHolder?.state?.putState(PerfSpec.toString(), specName)
    }

    override fun onAppBarButtonClick() {
        getHudFragment().onAppBarButtonClick()
    }

    override fun showSearch(query: String?) {
        val currentScreen = getDisplayedFragment()

        if (currentScreen is SearchFragment) {
            currentScreen.startQuery(query)
            return
        }

        showFragmentSimple {
            SearchFragment.newInstance(NullableStringArgs(query))
        }
    }

    override fun showFavorites(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showTopLevelFragment {
            FavoriteListFragment.newInstance()
        }
    }

    override fun showGameList(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showTopLevelFragment {
            GameListFragment.newInstance()
        }
    }

    override fun showComposerList(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showTopLevelFragment {
            ComposerListFragment.newInstance()
        }
    }

    override fun showTagList(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showTopLevelFragment {
            TagKeyListFragment.newInstance()
        }
    }

    override fun showAllSheets(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showTopLevelFragment {
            SongListFragment.newInstance()
        }
    }

    override fun showSettings(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showFragmentSimple {
            SettingFragment.newInstance()
        }
    }

    override fun showDebug(
        fromScreen: TrackingScreen?,
        fromDetails: String?
    ) {
        showFragmentSimple {
            DebugFragment.newInstance()
        }
    }

    override fun showAbout() {
        showFragmentSimple {
            AboutFragment.newInstance()
        }
    }

    override fun searchYoutube(name: String, gameName: String) {
        val query = "$gameName - $name music"
        val youtubeUrl = getYoutubeSearchUrlForQuery(query)

        goToWebUrl(youtubeUrl)
    }

    override fun goToWebUrl(url: String) {
        val launcher = Intent(Intent.ACTION_VIEW)
        launcher.data = Uri.parse(url)
        startActivity(launcher)
    }

    override fun showLicenseScreen() = showFragmentSimple {
        LicenseFragment.newInstance()
    }

    override fun back() {
        supportFragmentManager.popBackStack()
    }

    override fun showSongListForGame(gameId: Long) {
        showFragmentSimple {
            GameFragment.newInstance(IdArgs(gameId))
        }
    }

    override fun showSongListForComposer(composerId: Long) {
        showFragmentSimple {
            ComposerDetailFragment.newInstance(IdArgs(composerId))
        }
    }

    override fun showValueListForTagKey(tagKeyId: Long) = showFragmentSimple {
        TagValueFragment.newInstance(IdArgs(tagKeyId))
    }

    override fun showSongListForTagValue(tagValueId: Long) = showFragmentSimple {
        TagValueSongFragment.newInstance(IdArgs(tagValueId))
    }

    override fun showSheetDetail(songId: Long) = showFragmentSimple {
        SongFragment.newInstance(IdArgs(songId))
    }

    override fun showSongViewer(songId: Long?) {
        showFragmentSimple {
            ViewerFragment.newInstance(ViewerArgs(songId = songId))
        }
    }

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

    private fun showFragmentSimple(fragmentProvider: () -> VglsFragment) {
        runOnUiThread {
            val fragment = fragmentProvider.invoke()
            val displayedFragment = getDisplayedFragment()

            if (displayedFragment?.getVglsFragmentTag() != fragment.getVglsFragmentTag()) {
                if (supportFragmentManager.isDestroyed) {
                    return@runOnUiThread
                }

                val transaction = supportFragmentManager.beginTransaction()
                    .setDefaultAnimations()
                    .replace(R.id.frame_fragment, fragment)
                    .addToBackStack(null)

                if (!supportFragmentManager.isDestroyed) {
                    transaction.commit()
                }
            }
        }
    }

    private fun showTopLevelFragment(fragmentProvider: () -> VglsFragment) {
        runOnUiThread {
            val fragment = fragmentProvider.invoke()
            clearBackStack()

            if (supportFragmentManager.isDestroyed) {
                return@runOnUiThread
            }

            val transaction = supportFragmentManager.beginTransaction()
                .setDefaultAnimations()
                .replace(R.id.frame_fragment, fragment)

            if (!supportFragmentManager.isDestroyed) {
                transaction.commit()
            }
        }
    }

    private fun getHudFragment() =
        supportFragmentManager.findFragmentById(R.id.frame_hud) as HudFragment

    private fun getDisplayedFragment() =
        supportFragmentManager.findFragmentById(R.id.frame_fragment) as VglsFragment?

    private fun addHud() {
        if (supportFragmentManager.isDestroyed) {
            return
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.frame_hud, HudFragment.newInstance())
            .commit()
    }

    private fun initializeJankStats() {
        if (!BuildConfig.DEBUG) {
            return
        }

        hatchet.i(this.javaClass.simpleName, "Initializing JankStats.")

        jankStats = JankStats.createAndTrack(
            window
        ) {
            val spec = it
                .states
                .firstOrNull { state -> state.key == PerfSpec.toString() }
                ?.let { state -> PerfSpec.valueOf(state.value) }

            if (spec != null) {
                perfTracker.reportFrame(it.toFrameInfo(), spec)
            }
        }

        metricsStateHolder = PerformanceMetricsState.getHolderForHierarchy(
            window.findViewById(R.id.frame_fragment)
        )
    }

    private fun FrameData.toFrameInfo() = FrameInfo(
        frameStartNanos,
        frameDurationUiNanos,
        isJank
    )

    private fun FragmentTransaction.setDefaultAnimations() =
        setCustomAnimations(
            R.anim.enter,
            R.anim.exit,
            R.anim.enter_pop,
            R.anim.exit_pop
        )
}
