package com.vgleadsheets.images

import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

fun ImageView.loadImageLowQuality(path: String, fade: Boolean, placeholder: Int, callback: Callback? = null) {
    val requestCreator = Picasso.get()
        .load(path)
        .config(Bitmap.Config.RGB_565)
        .centerCrop()
        .fit()

    if (!fade) {
        requestCreator.noFade()
    }

    requestCreator.placeholder(placeholder)
    requestCreator.error(placeholder)

    callback?.let {
        requestCreator.into(this, callback)
    } ?: let {
        requestCreator.into(this)
    }
}

fun ImageView.loadImageHighQuality(path: String, fade: Boolean, placeholder: Int, callback: Callback? = null) {
    val requestCreator = Picasso.get()
        .load(path)
        .centerCrop()
        .fit()

    if (!fade) {
        requestCreator.noFade()
    }

    requestCreator.placeholder(placeholder)
    requestCreator.error(placeholder)

    callback?.let {
        requestCreator.into(this, callback)
    } ?: let {
        requestCreator.into(this)
    }
}

fun ImageView.loadImageHighQualityAspect(path: String, fade: Boolean, aspectRatio: Float?, callback: Callback? = null) {
    val bigWidth = width
    val bigHeight = if (aspectRatio != null) {
        getBigHeight(bigWidth, aspectRatio)
    } else {
        height
    }

    loadImageSetSize(path, bigWidth, bigHeight, fade, callback)
}

fun ImageView.loadImageSetSize(
    path: String,
    width: Int,
    height: Int,
    fade: Boolean = true,
    callback: Callback? = null
) {
    val requestCreator = Picasso.get()
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
