package com.vgleadsheets.di

import com.vgleadsheets.games.GameListFragment
import com.vgleadsheets.main.MainActivity
import com.vgleadsheets.search.SearchFragment
import com.vgleadsheets.songs.SongListFragment
import com.vgleadsheets.viewer.ViewerFragment
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
    internal abstract fun contributeSearchFragmentInjector(): SearchFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeSongListFragmentInjector(): SongListFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeViewerFragmentInjector(): ViewerFragment
}
