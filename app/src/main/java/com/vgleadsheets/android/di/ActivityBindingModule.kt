package com.vgleadsheets.android.di

import com.vgleadsheets.main.MainActivity
import dagger.android.ContributesAndroidInjector
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.Binds
import dagger.Module


@Module
internal abstract class ActivityBindingModule {
    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun contributeYourActivityInjector(): MainActivity
}

