package com.vgleadsheets.components

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.vgleadsheets.loadImageFull
import com.vgleadsheets.loadImageLowQuality
import timber.log.Timber

@BindingAdapter("sheetUrl", "listener")
fun bindImage(
    view: ImageView,
    sheetUrl: String,
    listener: SheetListModel.ImageListener
) {
    Timber.w("Loading image: ${sheetUrl.substringAfterLast("-")}")
    listener.onLoadStart(sheetUrl)

    val callback = object : Callback {
        override fun onSuccess() {
            listener.onLoadSuccess(sheetUrl)
        }

        override fun onError(e: Exception?) {
            listener.onLoadFailed(sheetUrl, e)
        }
    }

    view.loadImageFull(sheetUrl, callback)
}

@BindingAdapter("photoUrl")
fun bindPhoto(
    view: ImageView,
    photoUrl: String?
) {
    if (photoUrl != null) {
        view.loadImageLowQuality(photoUrl, true, true)
    } else {
        view.setImageResource(R.drawable.placeholder_game)
    }
}

@BindingAdapter("drawable")
fun bindDrawable(
    view: ImageView,
    drawable: Int
) {
    view.setImageResource(drawable)
}


@SuppressWarnings("unused")
@BindingAdapter("vglsId", "giantBombId", "name", "type", "handler")
fun bindGiantBombId(
    view: View,
    vglsId: Long,
    giantBombId: Long?,
    name: String,
    type: String,
    events: GiantBombImageNameCaptionListModel.EventHandler
) {
    // Just so unused check won't complain.
    if (giantBombId == null) {
        Timber.w("No GiantBomb ID found for ${view.javaClass.simpleName} with VGLS ID $vglsId: $name")
        events.onGbGameNotChecked(vglsId, name, type)
    }
}
