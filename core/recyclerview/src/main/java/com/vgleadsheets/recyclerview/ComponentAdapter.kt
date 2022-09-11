package com.vgleadsheets.recyclerview

import android.content.res.Resources
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.vgleadsheets.components.ComponentViewHolder
import com.vgleadsheets.components.ListModel
import timber.log.Timber

class ComponentAdapter(val owner: String) :
    ListAdapter<ListModel, ComponentViewHolder>(ComponentDiffer) {

    init {
        setHasStableIds(true)
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    var resources: Resources? = null

    fun getFirstItem() = if (itemCount > 0) {
        getItem(0)
    } else null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            viewType,
            parent,
            false
        )

        return ComponentViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun onBindViewHolder(
        holder: ComponentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.bind(getItem(position), payloads)
    }

    override fun getItemId(position: Int) = getItem(position).dataId

    override fun getItemViewType(position: Int) = getItem(position).layoutId

    override fun submitList(list: List<ListModel>?) {
        if (!isNewListActuallyDifferent(list)) return

        checkDupes(list)
        super.submitList(list)
    }

    @Suppress("MagicNumber")
    fun submitListAnimateResizeContainer(list: List<ListModel>?, container: ViewGroup?) {
        if (!isNewListActuallyDifferent(list)) return

        checkDupes(list)
        super.submitList(list) {
            container?.let {
                val transition = ChangeBounds()

                transition.interpolator = DecelerateInterpolator(2.0f)
                transition.duration = 300L

                TransitionManager.beginDelayedTransition(it, transition)
            }
        }
    }

    private fun checkDupes(list: List<ListModel>?) {
        val ids = list?.map {
            it.dataId
        }

        val distinctIds = ids?.distinct()

        if (ids?.size != distinctIds?.size) {
            reportDuplicateModel(list)
        }
    }

    @Suppress("SwallowedException")
    private fun reportDuplicateModel(list: List<ListModel>?) {
        val duplicateModels = list!!
            .groupBy { it.dataId }
            .filter { it.value.size > 1 }

        val builder = StringBuilder("$owner: Dataset contains duplicate ids!\n")
        duplicateModels.forEach { entry ->
            val duplicateId = entry.key

            val duplicateIdName = try {
                resources?.getResourceName(duplicateId.toInt())
            } catch (ex: Resources.NotFoundException) {
                null
            }

            builder.append("ListModels with id ${duplicateIdName ?: duplicateId}:\n")
            val modelsWithThisId = entry.value

            modelsWithThisId.forEach { model ->
                val toAppend = if (model.doesNonNullHandlerExist()) {
                    model.javaClass.simpleName
                } else {
                    model.toString()
                }
                builder.append("$toAppend\n")
            }
        }

        throw IllegalStateException(builder.toString())
    }

    private fun isNewListActuallyDifferent(newList: List<ListModel>?) =
        if (!currentList.deepEquals(newList)) {
            Timber.w("$owner: Lists changed, submitting.")
            true
        } else {
            Timber.i("$owner: Lists equivalent, not submitting.")
            false
        }

    private fun <E> List<E>?.deepEquals(otherList: List<E>?): Boolean {
        if (this == null && otherList != null) return false
        if (otherList == null && this != null) return false

        if (otherList?.size != this?.size) {
            if (BuildConfig.DEBUG) {
                Timber.v("$owner: sizes differ - old size ${this?.size ?: 0}; new size ${otherList?.size ?: 0}")
            }
            return false
        }

        this?.forEachIndexed { index, item ->
            val otherItem = otherList?.get(index)

            if (otherItem != item) {
                if (BuildConfig.DEBUG) {
                    Timber.v("$owner: items at $index differ - old item $item; new item $otherItem")
                }
                return false
            }
        }

        return true
    }

    private fun Any.doesNonNullHandlerExist(): Boolean {
        val methods = javaClass.methods
        for (method in methods) {
            if (method.name == "getHandler") {
                val value = method.invoke(this)
                return value != null
            }
        }
        return false
    }
}
