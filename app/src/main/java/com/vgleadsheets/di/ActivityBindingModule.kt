package com.vgleadsheets.di

import com.vgleadsheets.features.main.composer.ComposerFragment
import com.vgleadsheets.features.main.composer.ComposerListFragment
import com.vgleadsheets.features.main.game.GameFragment
import com.vgleadsheets.features.main.games.GameListFragment
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.search.SearchFragment
import com.vgleadsheets.features.main.songs.SongListFragment
import com.vgleadsheets.features.main.viewer.ViewerFragment
import com.vgleadsheets.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module()
internal abstract class ActivityBindingModule {
    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun contributeMainActivityInjector(): MainActivity

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeGameListFragmentInjector(): GameListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeGameFragmentInjector(): GameFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeComposerFragmentInjector(): ComposerFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeSearchFragmentInjector(): SearchFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeComposerListFragmentInjector(): ComposerListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeSongListFragmentInjector(): SongListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeViewerFragmentInjector(): ViewerFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeHudFragmentInjector(): HudFragment
}
