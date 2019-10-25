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