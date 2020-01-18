package com.vgleadsheets.components

import android.annotation.SuppressLint
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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

@SuppressLint("BinaryOperationInTimber")
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

    if (giantBombId == NO_API_KEY) {
        Timber.w(
            "Can't get metadata from Giant Bomb without an API key." +
                    "See instructions in README.MD and make sure to clear app data after" +
                    "rebuilding."
        )
        events.onGbApiNotAvailable()
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
    view.getPulseAnimator(model.listPosition * MULTIPLIER_LIST_POSITION % MAXIMUM_LOAD_OFFSET)
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
    view.setText(valueToDisplay, false);

    view.setOnItemClickListener { _, _, clickedPosition, _ ->
        itemSelectListener.onNewOptionSelected(settingId, clickedPosition)
    }
}

const val MULTIPLIER_LIST_POSITION = 100
const val MAXIMUM_LOAD_OFFSET = 250

const val NO_API_KEY = -5678L
