package com.vgleadsheets.di

import com.vgleadsheets.features.main.about.AboutFragment
import com.vgleadsheets.features.main.composer.ComposerDetailFragment
import com.vgleadsheets.features.main.composers.ComposerListFragment
import com.vgleadsheets.features.main.debug.DebugFragment
import com.vgleadsheets.features.main.favorites.FavoriteListFragment
import com.vgleadsheets.features.main.game.GameFragment
import com.vgleadsheets.features.main.games.GameListFragment
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.license.LicenseFragment
import com.vgleadsheets.features.main.search.SearchFragment
import com.vgleadsheets.features.main.settings.SettingFragment
import com.vgleadsheets.features.main.sheet.SongFragment
import com.vgleadsheets.features.main.songs.SongListFragment
import com.vgleadsheets.features.main.tagkeys.TagKeyListFragment
import com.vgleadsheets.features.main.tagsongs.TagValueSongFragment
import com.vgleadsheets.features.main.tagvalues.TagValueFragment
import com.vgleadsheets.features.main.viewer.ViewerFragment
import com.vgleadsheets.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityBindingModule {
    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun contributeMainActivityInjector(): MainActivity

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributGBetterGameListFragmentInjector(): GameListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterFavoriteFragmentInjector(): FavoriteListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterGameFragmentInjector(): GameFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterComposerFragmentInjector(): ComposerDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterSearchFragmentInjector(): SearchFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterSettingsFragmentInjector(): SettingFragment

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
    internal abstract fun contributeBetterTagKeyFragmentInjector(): TagKeyListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterTagValueSongListFragmentInjector(): TagValueSongFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterTagValueFragmentInjector(): TagValueFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterSongListFragmentInjector(): SongListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterSongDetailFragmentInjector(): SongFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeViewerFragmentInjector(): ViewerFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeBetterDebugFragmentInjector(): DebugFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeHudFragmentInjector(): HudFragment
}
