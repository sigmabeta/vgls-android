package com.vgleadsheets.jvm.di

import com.vgleadsheets.network.FakeModelGenerator
import com.vgleadsheets.network.FakeSheetDownloadApi
import com.vgleadsheets.network.FakeVglsApi
import com.vgleadsheets.network.SheetDownloadApi
import com.vgleadsheets.network.SheetDownloadApiImpl
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.network.VglsApiImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import net.sigmabeta.sage.di.AppScope

/** Desktop mirror of the android ApiModule — same ktor-backed VglsApi / SheetDownloadApi. */
@BindingContainer
@ContributesTo(AppScope::class)
object JvmApiModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideVglsApi(
        @Named("VglsApiUrl") baseUrl: String?,
        client: HttpClient,
        fakeModelGenerator: FakeModelGenerator,
    ): VglsApi = if (baseUrl != null) {
        VglsApiImpl(client, baseUrl)
    } else {
        FakeVglsApi(fakeModelGenerator)
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideSheetDownloadApi(
        @Named("VglsPdfUrl") baseUrl: String?,
        client: HttpClient,
    ): SheetDownloadApi = if (baseUrl != null) {
        SheetDownloadApiImpl(client, baseUrl)
    } else {
        FakeSheetDownloadApi()
    }
}
