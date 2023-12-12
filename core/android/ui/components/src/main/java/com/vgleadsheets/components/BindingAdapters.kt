@file:Suppress("TooManyFunctions")

package com.vgleadsheets.components

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.postDelayed
import androidx.databinding.BindingAdapter
import com.vgleadsheets.animation.pulseAnimator
import com.vgleadsheets.ui.components.R
import com.vgleadsheets.ui.themes.VglsMaterial
import com.vgleadsheets.ui.themes.VglsMaterialMenu

@BindingAdapter("starFillThreshold", "stars")
fun bindStarFilling(
    view: ImageView,
    starFillThreshold: Int,
    stars: Int
) {
    val starResource = if (stars >= starFillThreshold) {
        com.vgleadsheets.ui.icons.R.drawable.ic_jam_filled
    } else {
        com.vgleadsheets.ui.icons.R.drawable.ic_jam_unfilled
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
    view.pulseAnimator(model.dataId.toInt() * MULTIPLIER_LIST_POSITION % MAXIMUM_LOAD_OFFSET)
        .start()
}

@BindingAdapter("model")
fun bindNameCaptionLoading(view: ConstraintLayout, model: LoadingNameCaptionListModel) {
    view.pulseAnimator(model.dataId.toInt() * MULTIPLIER_LIST_POSITION % MAXIMUM_LOAD_OFFSET)
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
        ContextCompat.getColor(
            view.context,
            com.vgleadsheets.colors.R.color.md_theme_light_tertiaryContainer
        )
    } else {
        ContextCompat.getColor(
            view.context,
            com.vgleadsheets.colors.R.color.md_theme_light_onPrimary
        )
    }

    view.setTextColor(color)
}

@BindingAdapter("highlighted")
fun setHighlighting(
    view: ImageView,
    highlighted: Boolean
) {
    val color = if (highlighted) {
        ContextCompat.getColor(
            view.context,
            com.vgleadsheets.colors.R.color.md_theme_light_tertiaryContainer
        )
    } else {
        ContextCompat.getColor(
            view.context,
            com.vgleadsheets.colors.R.color.md_theme_light_onPrimary
        )
    }

    view.setColorFilter(color)
}

@BindingAdapter("query")
fun searchQuery(
    view: EditText,
    query: String?
) {
    if (query != view.text.toString()) {
        if (query.isNullOrEmpty()) {
            view.setText(query)
            view.postDelayed(DELAY_KEYBOARD_FOCUS) {
                view.requestFocus()

                val imm = ContextCompat.getSystemService(
                    view.context,
                    InputMethodManager::class.java
                )
                imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }
}

@BindingAdapter("textEntered")
fun searchQuery(
    view: EditText,
    onTextEntered: (String) -> Unit,
) {
    view.addTextChangedListener(
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(enteredText: Editable?) {
                onTextEntered(enteredText.toString())
            }
        }
    )
}

@BindingAdapter("composableModel")
fun composeView(view: ComposeView, composableModel: ListModel) {
    view.setContent {
        VglsMaterial {
            composableModel.Content(modifier = Modifier)
        }
    }
}

@BindingAdapter("composableMenuModel")
fun composeViewInMenu(view: ComposeView, composableMenuModel: ListModel) {
    view.setContent {
        VglsMaterialMenu {
            composableMenuModel.Content(modifier = Modifier)
        }
    }
}

const val DELAY_KEYBOARD_FOCUS = 50L

const val MULTIPLIER_LIST_POSITION = 100
const val MAXIMUM_LOAD_OFFSET = 250
