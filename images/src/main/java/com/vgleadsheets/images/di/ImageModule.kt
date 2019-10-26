package com.vgleadsheets.images.di

import com.squareup.picasso.OkHttp3Downloader
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Named

@Module
class ImageModule {
    @Provides
    fun providePicassoDownloader(@Named("PicassoOkHttp") okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }
}
