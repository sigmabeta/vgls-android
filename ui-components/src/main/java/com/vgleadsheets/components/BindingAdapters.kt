@file:Suppress("TooManyFunctions")

package com.vgleadsheets.components

import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.vgleadsheets.animation.getEndPulseAnimator
import com.vgleadsheets.animation.getPulseAnimator
import com.vgleadsheets.images.loadImageHighQuality
import com.vgleadsheets.images.loadImageLowQuality
import com.vgleadsheets.perf.tracking.common.PerfTracker
import kotlin.math.min

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

@BindingAdapter("bigPhotoUrl", "placeholder", "imageLoadedHandler", "screenName")
fun bindBigPhoto(
    view: ImageView,
    photoUrl: String?,
    placeholder: Int,
    imageLoadedHandler: PerfTracker,
    screenName: String
) {
    if (placeholder != R.drawable.ic_logo) {
        view.clipToOutline = true
        view.setBackgroundResource(R.drawable.background_image_circle)
    }

    if (photoUrl != null) {
        val callback = object : Callback {
            override fun onSuccess() {
                imageLoadedHandler.onTransitionStarted(screenName)
            }

            override fun onError(e: java.lang.Exception?) {
                imageLoadedHandler.cancel(screenName)
            }
        }

        view.loadImageHighQuality(photoUrl, true, placeholder, callback)
    } else {
        if (placeholder != R.drawable.ic_logo) {
            imageLoadedHandler.cancel(screenName)
        } else {
            imageLoadedHandler.onTransitionStarted(screenName)
        }
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
    view.getPulseAnimator(model.listPosition * MULTIPLIER_LIST_POSITION % MAXIMUM_LOAD_OFFSET)
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

@BindingAdapter("settingId", "selectedPosition", "itemSelectListener")
fun bindDropdownListener(
    view: AutoCompleteTextView,
    settingId: String,
    selectedPosition: Int,
    itemSelectListener: DropdownSettingListModel.EventHandler
) {
    val valueToDisplay = view.adapter.getItem(selectedPosition).toString()
    view.setText(valueToDisplay, false)

    view.setOnItemClickListener { _, _, clickedPosition, _ ->
        itemSelectListener.onNewOptionSelected(settingId, clickedPosition)
    }
}

@BindingAdapter("model", "longClickHandler")
fun bindLongClickHandler(
    view: FrameLayout,
    model: ToolbarItemListModel,
    longClickHandler: ToolbarItemListModel.EventHandler
) {
    view.setOnLongClickListener {
        longClickHandler.onLongClicked(model)
        true
    }
}

@BindingAdapter("handler", "screenName")
fun bindCheckablePerfEvents(view: View?, handler: CheckableListModel.EventHandler, screenName: String) {
    if (view != null) {
        handler.onCheckboxLoadComplete(screenName)
    }
}

@BindingAdapter(
    "partialLoadText",
    "partialLoadHandler",
    "fullLoadText",
    "fullLoadHandler",
    "screenName"
)
fun bindPartialLoadHandler(
    view: View?,
    partialLoadText: String,
    partialLoadHandler: PerfTracker,
    fullLoadText: String,
    fullLoadHandler: PerfTracker,
    screenName: String
) {
    if (view != null && partialLoadText.isNotEmpty()) {
        partialLoadHandler.onPartialContentLoad(screenName)
    }

    if (view != null && fullLoadText.isNotEmpty()) {
        fullLoadHandler.onFullContentLoad(screenName)
    }
}

@BindingAdapter("titleLoadedHandler", "screenName")
fun bindTitleLoadedHandler(view: View?, titleLoadedHandler: PerfTracker, screenName: String) {
    if (view != null) {
        titleLoadedHandler.onTitleLoaded(screenName)
    }
}

@BindingAdapter("startTime", "duration", "targetTime", "cancellation")
fun bindPerfBar(
    view: View,
    startTime: Long,
    duration: String,
    targetTime: Long,
    cancellation: Long?
) {
    view.pivotX = 0.0f

    val durationLong = duration.toLongOrNull()

    if (durationLong == null) {
        if (cancellation != null) {
            val cancelPercentage = cancellation.toFloat() / targetTime

            view.animate().cancel()
            view.scaleX = min(cancelPercentage, 1.0f)
            view.setBackgroundColor(
                ContextCompat.getColor(view.context, android.R.color.holo_orange_light)
            )

            return
        }

        val startPercentage = (System.currentTimeMillis().toFloat() - startTime) / targetTime
        val animationTime = (1.0f - startPercentage) * targetTime

        val startColor = if (startPercentage > 1.0f) {
            ContextCompat.getColor(view.context, android.R.color.holo_red_dark)
        } else {
            ContextCompat.getColor(view.context, R.color.colorPrimary)
        }

        view.setBackgroundColor(startColor)

        view.scaleX = min(startPercentage, 1.0f)
        view.animate()
            .scaleX(1.0f)
            .setInterpolator(LinearInterpolator())
            .setDuration(animationTime.toLong())
            .withEndAction {
                val endColor = ContextCompat.getColor(view.context, android.R.color.holo_red_dark)
                view.setBackgroundColor(endColor)
            }

    } else {
        val durationPercentage = durationLong.toFloat() / targetTime

        view.animate().cancel()
        view.scaleX = min(durationPercentage, 1.0f)

        val color = if (durationPercentage > 1.0f) {
            ContextCompat.getColor(view.context, android.R.color.holo_red_dark)
        } else {
            ContextCompat.getColor(view.context, R.color.colorPrimary)
        }

        view.setBackgroundColor(color)
    }
}

const val MULTIPLIER_LIST_POSITION = 100
const val MAXIMUM_LOAD_OFFSET = 250
