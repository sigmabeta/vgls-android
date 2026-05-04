package com.vgleadsheets.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.vgleadsheets.analytics.firebase.VglsFirebaseAnalyticsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.coroutines.SageDispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object VglsAnalyticsModule {
    @Provides
    @Singleton
    fun provideVglsAnalyticsImpl(
        firebaseAnalytics: FirebaseAnalytics,
        dispatchers: SageDispatchers,
        coroutineScope: CoroutineScope,
    ): VglsFirebaseAnalyticsImpl = VglsFirebaseAnalyticsImpl(
        firebaseAnalytics,
        dispatchers,
        coroutineScope,
    )

    @Provides
    @Singleton
    fun provideAnalytics(impl: VglsFirebaseAnalyticsImpl): Analytics = impl

    @Provides
    @Singleton
    fun provideVglsAnalytics(impl: VglsFirebaseAnalyticsImpl): VglsAnalytics = impl
}
