@file:Suppress("TooManyFunctions")

package com.vgleadsheets.components

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.google.android.material.color.MaterialColors
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.vgleadsheets.animation.getEndPulseAnimator
import com.vgleadsheets.animation.getPulseAnimator
import com.vgleadsheets.images.loadImageHighQuality
import com.vgleadsheets.images.loadImageLowQuality

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

    listener.onLoadStarted()

    val callback = object : Callback {
        override fun onSuccess() {
            listener.onLoadComplete()
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
    view.clipToOutline = true

    if (photoUrl != null) {
        view.loadImageLowQuality(photoUrl, true, placeholder)
    } else {
        view.setImageResource(placeholder)
    }
}

@BindingAdapter("bigPhotoUrl", "placeholder", "imageLoadSuccess", "imageLoadFail")
fun bindBigPhoto(
    view: ImageView,
    photoUrl: String?,
    placeholder: Int,
    imageLoadSuccess: () -> Unit,
    imageLoadFail: (Exception) -> Unit
) {
    if (placeholder != R.drawable.ic_logo) {
        view.clipToOutline = true
        view.setBackgroundResource(R.drawable.background_image_circle)
    }

    if (photoUrl != null) {
        val callback = object : Callback {
            override fun onSuccess() {
                imageLoadSuccess()
            }

            override fun onError(e: Exception) {
                imageLoadFail(e)
            }
        }

        view.loadImageHighQuality(photoUrl, true, placeholder, callback)
    } else {
        view.setImageResource(placeholder)
    }
}

@BindingAdapter("starFillThreshold", "stars")
fun bindStarFilling(
    view: ImageView,
    starFillThreshold: Int,
    stars: Int
) {
    val starResource = if (stars >= starFillThreshold) {
        R.drawable.ic_jam_filled
    } else {
        R.drawable.ic_jam_unfilled
    }

    view.setImageResource(starResource)
}

@BindingAdapter("drawable")
fun bindDrawable(
    view: ImageView,
    drawable: Int
) {
    view.setImageResource(drawable)
}

@BindingAdapter("model")
fun bindImageNameCaptionLoading(view: ConstraintLayout, model: LoadingImageNameCaptionListModel) {
    view.getPulseAnimator(model.dataId.toInt() * MULTIPLIER_LIST_POSITION % MAXIMUM_LOAD_OFFSET)
        .start()
}

@BindingAdapter("model")
fun bindTitleLoading(view: LinearLayout, model: LoadingTitleListModel) {
    view.getPulseAnimator(model.dataId.toInt() * MULTIPLIER_LIST_POSITION % MAXIMUM_LOAD_OFFSET)
        .start()
}

@BindingAdapter("model")
fun bindNameCaptionLoading(view: ConstraintLayout, model: LoadingNameCaptionListModel) {
    view.getPulseAnimator(model.dataId.toInt() * MULTIPLIER_LIST_POSITION % MAXIMUM_LOAD_OFFSET)
        .start()
}

@BindingAdapter("model")
fun bindCheckableLoading(view: LinearLayout, model: LoadingCheckableListModel) {
    view.getPulseAnimator(model.loadPositionOffset * MULTIPLIER_LIST_POSITION % MAXIMUM_LOAD_OFFSET)
        .start()
}

@BindingAdapter("labels")
fun bindDropdownValues(view: AutoCompleteTextView, labels: List<String>) {
    val adapter = ArrayAdapter<String>(
        view.context,
        R.layout.dropdown_item,
        labels
    )

    view.setAdapter(adapter)
}

@BindingAdapter("selectedPosition", "onNewOptionSelected")
fun bindDropdownListener(
    view: AutoCompleteTextView,
    selectedPosition: Int,
    onNewOptionSelected: (Int) -> Unit,
) {
    val valueToDisplay = view.adapter.getItem(selectedPosition).toString()
    view.setText(valueToDisplay, false)

    view.setOnItemClickListener { _, _, clickedPosition, _ ->
        onNewOptionSelected(clickedPosition)
    }
}

@BindingAdapter("icon")
fun bindIcon(view: ImageButton, iconId: Int) {
    view.setImageResource(iconId)
}

@BindingAdapter("highlighted")
fun setHighlighting(
    view: TextView,
    highlighted: Boolean
) {
    val color = if (highlighted) {
        MaterialColors.getColor(view, R.attr.colorTertiaryContainer)
    } else {
        MaterialColors.getColor(view, R.attr.colorOnPrimary)
    }

    view.setTextColor(color)
}

@BindingAdapter("highlighted")
fun setHighlighting(
    view: ImageView,
    highlighted: Boolean
) {
    val color = if (highlighted) {
        MaterialColors.getColor(view, R.attr.colorTertiaryContainer)
    } else {
        MaterialColors.getColor(view, R.attr.colorOnPrimary)
    }

    view.setColorFilter(color)
}

const val MULTIPLIER_LIST_POSITION = 100
const val MAXIMUM_LOAD_OFFSET = 250
