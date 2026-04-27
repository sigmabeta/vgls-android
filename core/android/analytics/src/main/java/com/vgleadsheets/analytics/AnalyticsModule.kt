package com.vgleadsheets.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.vgleadsheets.analytics.firebase.FirebaseAnalyticsImpl
import com.vgleadsheets.coroutines.VglsDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AnalyticsModule {
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context) = FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    fun provideAnalyticsImpl(
        firebaseAnalytics: FirebaseAnalytics,
        dispatchers: VglsDispatchers,
        coroutineScope: CoroutineScope
    ): Analytics = FirebaseAnalyticsImpl(
            firebaseAnalytics,
            dispatchers,
            coroutineScope,
        )
}
