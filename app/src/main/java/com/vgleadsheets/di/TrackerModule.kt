package com.vgleadsheets.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.firebase.FirebaseTracker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TrackerModule() {
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(context: Context) = FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    fun provideTracker(firebaseAnalytics: FirebaseAnalytics): Tracker = FirebaseTracker(firebaseAnalytics)
}
