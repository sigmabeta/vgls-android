package com.vgleadsheets.di

// TODO Move this file into the actual analytics  modules.

// import com.google.firebase.perf.FirebasePerformance
// import com.vgleadsheets.perf.tracking.common.PerfTrackingBackend
// import com.vgleadsheets.perf.tracking.firebase.FirebasePerfBackend
// import com.vgleadsheets.tracking.Tracker
// import dagger.Module
// import dagger.Provides
// import dagger.hilt.InstallIn
// import dagger.hilt.components.SingletonComponent
// import javax.inject.Singleton
//
// @InstallIn(SingletonComponent::class)
// @Module
// object PerfBackendModule {
//     @Provides
//     @Singleton
//     fun provideFirebasePerfInstance(): FirebasePerformance = FirebasePerformance.getInstance()
//
//     @Provides
//     @Singleton
//     fun providePerfBackend(
//         firebase: FirebasePerformance,
//         tracker: Tracker
//     ): PerfTrackingBackend = FirebasePerfBackend(
//         firebase,
//         tracker
//     )
// }
