package com.vgleadsheets.components

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class ComponentViewHolder(
    private val binding: ViewDataBinding,
    private val clickBridge: AdapterVhClickBridge
) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener {
    init {
        binding.root.setOnClickListener(this)
    }

    fun bind(modelToBind: Any) {
        Timber.i("$modelToBind")
        binding.setVariable(BR.toBind, modelToBind)
        binding.executePendingBindings()
    }

    override fun onClick(clicked: View) = clickBridge.onComponentClickedAtPosition(adapterPosition)
}

