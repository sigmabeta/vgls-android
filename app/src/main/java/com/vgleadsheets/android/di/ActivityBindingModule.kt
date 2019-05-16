package com.vgleadsheets.android.di

import com.vgleadsheets.di.ActivityScope
import com.vgleadsheets.di.FragmentScope
import com.vgleadsheets.games.GameListFragment
import com.vgleadsheets.main.MainActivity
import dagger.android.ContributesAndroidInjector
import dagger.Module


@Module()
internal abstract class ActivityBindingModule {
    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun contributeMainActivityInjector(): MainActivity

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeGameListFragmentInjector(): GameListFragment
}

