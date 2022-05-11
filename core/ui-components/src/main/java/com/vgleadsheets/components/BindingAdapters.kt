@file:Suppress("TooManyFunctions")

package com.vgleadsheets.components

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
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

@BindingAdapter("bigPhotoUrl", "placeholder")
fun bindBigPhoto(
    view: ImageView,
    photoUrl: String?,
    placeholder: Int,
) {
    if (placeholder != R.drawable.ic_logo) {
        view.clipToOutline = true
        view.setBackgroundResource(R.drawable.background_image_circle)
    }

    if (photoUrl != null) {
        val callback = object : Callback {
            override fun onSuccess() {}

            override fun onError(e: java.lang.Exception?) {}
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

@BindingAdapter("expanded")
fun bindExpanded(view: ImageButton, expanded: Boolean) {
    val iconId = if (expanded) R.drawable.ic_clear_black_24dp else R.drawable.ic_menu_24dp
    view.setImageResource(iconId)
}

const val MULTIPLIER_LIST_POSITION = 100
const val MAXIMUM_LOAD_OFFSET = 250
