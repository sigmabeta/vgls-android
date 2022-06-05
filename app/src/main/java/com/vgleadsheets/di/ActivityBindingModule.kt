package com.vgleadsheets.di

import com.vgleadsheets.features.main.about.AboutFragment
import com.vgleadsheets.features.main.composer.ComposerFragment
import com.vgleadsheets.features.main.composer.better.BetterComposerFragment
import com.vgleadsheets.features.main.composers.ComposerListFragment
import com.vgleadsheets.features.main.composers.better.BetterComposerListFragment
import com.vgleadsheets.features.main.debug.DebugFragment
import com.vgleadsheets.features.main.game.GameFragment
import com.vgleadsheets.features.main.game.better.BetterGameFragment
import com.vgleadsheets.features.main.games.GameListFragment
import com.vgleadsheets.features.main.games.better.BetterGameListFragment
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.jam.JamFragment
import com.vgleadsheets.features.main.jams.FindJamDialogFragment
import com.vgleadsheets.features.main.jams.JamListFragment
import com.vgleadsheets.features.main.license.LicenseFragment
import com.vgleadsheets.features.main.search.SearchFragment
import com.vgleadsheets.features.main.settings.SettingsFragment
import com.vgleadsheets.features.main.sheet.SheetDetailFragment
import com.vgleadsheets.features.main.songs.SongListFragment
import com.vgleadsheets.features.main.songs.better.BetterSongListFragment
import com.vgleadsheets.features.main.tagkeys.TagKeyFragment
import com.vgleadsheets.features.main.tagsongs.TagValueSongListFragment
import com.vgleadsheets.features.main.tagvalues.TagValueListFragment
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
    internal abstract fun contributeGameListFragmentInjector(): GameListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributGBetterGameListFragmentInjector(): BetterGameListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeJamListFragmentInjector(): JamListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeJamFragmentInjector(): JamFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeAddJamFragmentInjector(): FindJamDialogFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeGameFragmentInjector(): GameFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterGameFragmentInjector(): BetterGameFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeComposerFragmentInjector(): ComposerFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterComposerFragmentInjector(): BetterComposerFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeSearchFragmentInjector(): SearchFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeSettingsFragmentInjector(): SettingsFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeAboutFragmentInjector(): AboutFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeLicenseFragmentInjector(): LicenseFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeComposerListFragmentInjector(): ComposerListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterComposerListFragmentInjector(): BetterComposerListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeTagKeyFragmentInjector(): TagKeyFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeTagValueSongListFragmentInjector(): TagValueSongListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeTagValueFragmentInjector(): TagValueListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeSongListFragmentInjector(): SongListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterSongListFragmentInjector(): BetterSongListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeSheetDetailFragmentInjector(): SheetDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeViewerFragmentInjector(): ViewerFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeDebugFragmentInjector(): DebugFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeHudFragmentInjector(): HudFragment
}
