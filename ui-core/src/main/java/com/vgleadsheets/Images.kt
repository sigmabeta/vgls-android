package com.vgleadsheets


import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Single
import timber.log.Timber


fun loadBitmapLowQuality(context: Context, path: String): Single<Bitmap> {
    return Single.create {
        val bitmap = Picasso.with(context)
            .load(path)
            .config(Bitmap.Config.RGB_565)
            .resize(400, 400)
            .centerCrop()
            .get()

        it.onSuccess(bitmap)
    }
}

fun ImageView.loadImageLowQuality(path: String, fade: Boolean, placeholder: Boolean, callback: Callback? = null) {
    val requestCreator = Picasso.with(context)
        .load(path)
        .config(Bitmap.Config.RGB_565)
        .centerCrop()
        .fit()

    if (!fade) {
        requestCreator.noFade()
    }

    if (!placeholder) {
        requestCreator.noPlaceholder()
    }

    callback?.let {
        requestCreator.into(this, callback)
    } ?: let {
        requestCreator.into(this)
    }
}

fun ImageView.loadImageHighQuality(path: String, fade: Boolean, aspectRatio: Float?, callback: Callback? = null) {
    val bigWidth = width
    val bigHeight = if (aspectRatio != null) {
        getBigHeight(bigWidth, aspectRatio)
    } else {
        height
    }

    loadImageSetSize(path, bigWidth, bigHeight, callback = callback)
}

fun ImageView.loadImageSetSize(path: String,
                               width: Int,
                               height: Int,
                               fade: Boolean = true,
                               callback: Callback? = null) {
    Timber.v("Loading ${width}x${height} image into ${this.width}x${this.height} view")

    val requestCreator = Picasso.with(context)
        .load(path)
        .resize(width, height)
        .centerCrop()
        .noPlaceholder()

    if (!fade) {
        requestCreator.noFade()
    }

    requestCreator
        .into(this, callback)
}

fun getBigHeight(bigWidth: Int, aspectRatio: Float) =
    (bigWidth / aspectRatio).toInt()

fun calculateAspectRatio(width: Int, height: Int) = width / height.toFloat()
