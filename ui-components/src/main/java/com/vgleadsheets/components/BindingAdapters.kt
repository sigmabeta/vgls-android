package com.vgleadsheets.components

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.vgleadsheets.loadImageFull
import com.vgleadsheets.loadImageHighQuality
import com.vgleadsheets.loadImageLowQuality
import timber.log.Timber

@BindingAdapter("sheetUrl", "listener")
fun bindImage(
    view: ImageView,
    sheetUrl: String,
    listener: SheetListModel.ImageListener
) {
    view.setOnClickListener { listener.onClicked() }
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

@BindingAdapter("photoUrl", "placeholder")
fun bindPhoto(
    view: ImageView,
    photoUrl: String?,
    placeholder: Int
) {
    if (photoUrl != null) {
        view.loadImageLowQuality(photoUrl, true, placeholder)
    } else {
        view.setImageResource(placeholder)
    }
}

@BindingAdapter("bigPhotoUrl", "placeholder")
fun bindBigPhoto(
    view: ImageView,
    photoUrl: String?,
    placeholder: Int
) {
    if (photoUrl != null) {
        view.loadImageHighQuality(photoUrl, true, placeholder)
    } else {
        view.setImageResource(placeholder)
    }
}

@BindingAdapter("thumbUrl", "placeholder")
fun bindThumb(
    view: ImageView,
    thumbUrl: String?,
    placeholder: Int
) {
    if (thumbUrl != null) {
        view.loadImageLowQuality(thumbUrl, true, placeholder)
    } else {
        view.setImageResource(placeholder)
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
fun bindGiantBombIdList(
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
        events.onGbModelNotChecked(vglsId, name, type)
    }
}


@SuppressWarnings("unused")
@BindingAdapter("vglsId", "giantBombId", "name", "handler")
fun bindGiantBombIdTitle(
    view: View,
    vglsId: Long,
    giantBombId: Long?,
    name: String,
    events: GiantBombTitleListModel.EventHandler
) {
    // Just so unused check won't complain.
    if (giantBombId == null) {
        Timber.w("No GiantBomb ID found for ${view.javaClass.simpleName} with VGLS ID $vglsId: $name")
        events.onGbModelNotChecked(vglsId, name)
    }
}
