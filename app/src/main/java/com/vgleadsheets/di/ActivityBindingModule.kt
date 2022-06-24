package com.vgleadsheets.di

import com.vgleadsheets.features.main.about.AboutFragment
import com.vgleadsheets.features.main.composer.ComposerDetailFragment
import com.vgleadsheets.features.main.composers.ComposerListFragment
import com.vgleadsheets.features.main.debug.BetterDebugFragment
import com.vgleadsheets.features.main.game.better.BetterGameFragment
import com.vgleadsheets.features.main.games.better.BetterGameListFragment
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.jam.better.BetterJamFragment
import com.vgleadsheets.features.main.jams.FindJamDialogFragment
import com.vgleadsheets.features.main.jams.better.BetterJamListFragment
import com.vgleadsheets.features.main.license.LicenseFragment
import com.vgleadsheets.features.main.search.better.BetterSearchFragment
import com.vgleadsheets.features.main.settings.better.BetterSettingFragment
import com.vgleadsheets.features.main.sheet.better.BetterSongFragment
import com.vgleadsheets.features.main.songs.better.BetterSongListFragment
import com.vgleadsheets.features.main.tagkeys.better.BetterTagKeyListFragment
import com.vgleadsheets.features.main.tagsongs.better.BetterTagValueSongFragment
import com.vgleadsheets.features.main.tagvalues.better.BetterTagValueFragment
import com.vgleadsheets.features.main.viewer.ViewerFragment
import com.vgleadsheets.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("TooManyFunctions")
internal abstract class ActivityBindingModule {
    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun contributeMainActivityInjector(): MainActivity

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributGBetterGameListFragmentInjector(): BetterGameListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterJamListFragmentInjector(): BetterJamListFragment
    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterJamFragmentInjector(): BetterJamFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeAddJamFragmentInjector(): FindJamDialogFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterGameFragmentInjector(): BetterGameFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterComposerFragmentInjector(): ComposerDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterSearchFragmentInjector(): BetterSearchFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterSettingsFragmentInjector(): BetterSettingFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeAboutFragmentInjector(): AboutFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeLicenseFragmentInjector(): LicenseFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterComposerListFragmentInjector(): ComposerListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterTagKeyFragmentInjector(): BetterTagKeyListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterTagValueSongListFragmentInjector(): BetterTagValueSongFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterTagValueFragmentInjector(): BetterTagValueFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterSongListFragmentInjector(): BetterSongListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterSongDetailFragmentInjector(): BetterSongFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeViewerFragmentInjector(): ViewerFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterDebugFragmentInjector(): BetterDebugFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeHudFragmentInjector(): HudFragment
}
