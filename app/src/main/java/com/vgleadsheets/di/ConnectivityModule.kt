package com.vgleadsheets.di

import android.content.Context
import com.vgleadsheets.connectivity.AndroidNetworkStatusProvider
import com.vgleadsheets.connectivity.NetworkStatusProvider
import com.vgleadsheets.logging.Hatchet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ConnectivityModule {
    @Provides
    @Singleton
    fun provideNetworkStatusProvider(
        @ApplicationContext context: Context,
        hatchet: Hatchet,
    ): NetworkStatusProvider = AndroidNetworkStatusProvider(context, hatchet)
}
