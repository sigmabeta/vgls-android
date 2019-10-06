package com.vgleadsheets.components

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.vgleadsheets.loadImageFull
import timber.log.Timber

@BindingAdapter("imageUrl", "listener")
fun bindImage(
    view: ImageView,
    url: String,
    listener: SheetListModel.ImageListener
) {
        Timber.w("Loading image: ${url.substringAfterLast("-")}")
        listener.onLoadStart(url)

        val callback = object : Callback {
            override fun onSuccess() {
                listener.onLoadSuccess(url)
            }

            override fun onError(e: Exception?) {
                listener.onLoadFailed(url, e)
            }
        }

        view.loadImageFull(url, callback)
}

@BindingAdapter("drawable")
fun bindDrawable(
    view: ImageView,
    drawable: Int
) {
    view.setImageResource(drawable)
}
