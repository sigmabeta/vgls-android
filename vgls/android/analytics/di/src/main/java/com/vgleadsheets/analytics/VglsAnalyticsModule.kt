package com.vgleadsheets.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.vgleadsheets.analytics.firebase.VglsFirebaseAnalyticsImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.di.AppScope

@BindingContainer
@ContributesTo(AppScope::class)
object VglsAnalyticsModule {
    @Provides
    @SingleIn(AppScope::class)
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
    @SingleIn(AppScope::class)
    fun provideAnalytics(impl: VglsFirebaseAnalyticsImpl): Analytics = impl

    @Provides
    @SingleIn(AppScope::class)
    fun provideVglsAnalytics(impl: VglsFirebaseAnalyticsImpl): VglsAnalytics = impl
}
