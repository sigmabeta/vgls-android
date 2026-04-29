package com.vgleadsheets.appcomm.di

import com.vgleadsheets.appcomm.GenericAction
import com.vgleadsheets.appcomm.VglsAction
import net.sigmabeta.sage.logging.Hatchet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionDeserializer @Inject constructor(
    private val hatchet: Hatchet,
) {
    fun recreateAction(genericAction: GenericAction?): VglsAction? = when (genericAction?.type) {
            "VglsAction.RefreshDbClicked" -> VglsAction.RefreshDbClicked
            "VglsAction.DbSeeWhatsNewClicked" -> VglsAction.DbSeeWhatsNewClicked
            "VglsAction.AppSeeWhatsNewClicked" -> VglsAction.AppSeeWhatsNewClicked
            null -> null
            else -> fromGeneric(genericAction)
        }

    fun serializeAction(action: VglsAction?): GenericAction? = if (action != null) {
            toGeneric(action)
        } else {
            null
        }

    @Suppress("TooGenericExceptionCaught")
    private fun fromGeneric(genericAction: GenericAction): VglsAction? = try {
            when (genericAction.type) {
                "VglsAction.InitWithId" -> VglsAction.InitWithId(id = genericAction.argIdOne!!)
                else -> null
            }
        } catch (ex: NullPointerException) {
            hatchet.e("Invalid arguments to action: ${ex.message}")
            null
        }

    private fun toGeneric(action: VglsAction): GenericAction = when (action) {
            is VglsAction.RefreshDbClicked -> GenericAction(type = "VglsAction.RefreshDbClicked")
            is VglsAction.DbSeeWhatsNewClicked -> GenericAction(type = "VglsAction.DbSeeWhatsNewClicked")
            is VglsAction.AppSeeWhatsNewClicked -> GenericAction(type = "VglsAction.AppSeeWhatsNewClicked")
            is VglsAction.InitWithId -> GenericAction(type = "VglsAction.InitWithId", argIdOne = action.id)
            else -> throw IllegalArgumentException("Not a supported action: $action")
        }
}
