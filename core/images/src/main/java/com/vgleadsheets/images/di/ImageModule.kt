package com.vgleadsheets.images.di

import com.squareup.picasso.OkHttp3Downloader
import dagger.Module
import dagger.Provides
import javax.inject.Named
import okhttp3.OkHttpClient

@Module
class ImageModule {
    @Provides
    fun providePicassoDownloader(@Named("PicassoOkHttp") okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }
}
