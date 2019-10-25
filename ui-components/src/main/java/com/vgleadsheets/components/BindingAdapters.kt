package com.vgleadsheets.components

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.vgleadsheets.animation.getEndPulseAnimator
import com.vgleadsheets.animation.getPulseAnimator
import com.vgleadsheets.images.loadImageHighQuality
import com.vgleadsheets.images.loadImageLowQuality
import timber.log.Timber

@BindingAdapter("sheetUrl", "listener")
fun bindSheetImage(
    view: ImageView,
    sheetUrl: String,
    listener: SheetListModel.ImageListener
) {
    view.setOnClickListener { listener.onClicked() }

    val pulseAnimator = view.getPulseAnimator(
        sheetUrl.hashCode() % MAXIMUM_LOAD_OFFSET
    )
    pulseAnimator.start()

    val callback = object : Callback {
        override fun onSuccess() {
            pulseAnimator.cancel()
            view.getEndPulseAnimator().start()
        }

        override fun onError(e: Exception?) {
            pulseAnimator.cancel()
            view.getEndPulseAnimator().start()
            listener.onLoadFailed(sheetUrl, e)
        }
    }

    Picasso.get()
        .load(sheetUrl)
        .fit()
        .centerInside()
        .placeholder(R.drawable.ic_description_white_24dp)
        .error(R.drawable.ic_error_white_24dp)
        .into(view, callback)
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

@SuppressWarnings("LongParameterList")
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

@BindingAdapter("model")
fun bindNameCaptionLoading(view: ConstraintLayout, model: LoadingNameCaptionListModel) {
    view.getPulseAnimator(model.listPosition * 100 % MAXIMUM_LOAD_OFFSET).start()
}

const val MAXIMUM_LOAD_OFFSET = 250
