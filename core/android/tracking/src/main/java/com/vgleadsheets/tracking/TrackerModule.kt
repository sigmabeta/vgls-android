package com.vgleadsheets.tracking

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.vgleadsheets.tracking.firebase.FirebaseTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TrackerModule {
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context) = FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    fun provideTracker(firebaseAnalytics: FirebaseAnalytics): Tracker =
        FirebaseTracker(firebaseAnalytics)
}
