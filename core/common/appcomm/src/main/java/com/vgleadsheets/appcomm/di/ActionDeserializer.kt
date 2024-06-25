package com.vgleadsheets.appcomm.di

import com.vgleadsheets.appcomm.GenericAction
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.logging.Hatchet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionDeserializer @Inject constructor(
    private val hatchet: Hatchet,
) {
    fun recreateAction(genericAction: GenericAction?): VglsAction? {
        return when (genericAction?.type) {
            VglsAction.RefreshDbClicked::class.simpleName -> VglsAction.RefreshDbClicked
            VglsAction.SeeWhatsNewClicked::class.simpleName -> VglsAction.SeeWhatsNewClicked
            null -> null
            else -> fromGeneric(genericAction)
        }
    }

    fun serializeAction(action: VglsAction): GenericAction? {
        return toGeneric(action)
    }

    private fun fromGeneric(genericAction: GenericAction): VglsAction? {
        return try {
            when (genericAction.type) {
                VglsAction.InitWithId::class.simpleName -> VglsAction.InitWithId(id = genericAction.argIdOne!!)
                else -> null
            }
        } catch (ex: NullPointerException) {
            hatchet.e("Invalid arguments to action: ${ex.message}")
            null
        }
    }

    private fun toGeneric(action: VglsAction): GenericAction {
        return when (action) {
            is VglsAction.RefreshDbClicked -> GenericAction(type = action::class.java.simpleName)
            is VglsAction.SeeWhatsNewClicked -> GenericAction(type = action::class.java.simpleName)
            is VglsAction.InitWithId -> GenericAction(type = action::class.java.simpleName, argIdOne = action.id)
            else -> throw IllegalArgumentException("Not a supported action: $action")
        }
    }
}
