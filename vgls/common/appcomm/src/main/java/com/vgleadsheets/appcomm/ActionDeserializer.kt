package com.vgleadsheets.appcomm

import net.sigmabeta.sage.appcomm.GenericAction
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.logging.Hatchet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionDeserializer @Inject constructor(
    private val hatchet: Hatchet,
) {
    fun recreateAction(genericAction: GenericAction?): SageAction? = when (genericAction?.type) {
        "VglsAction.RefreshDbClicked" -> VglsAction.RefreshDbClicked
        "VglsAction.DbSeeWhatsNewClicked" -> VglsAction.DbSeeWhatsNewClicked
        "VglsAction.AppSeeWhatsNewClicked" -> VglsAction.AppSeeWhatsNewClicked
        null -> null
        else -> fromGeneric(genericAction)
    }

    fun serializeAction(action: SageAction?): GenericAction? = if (action != null) {
        toGeneric(action)
    } else {
        null
    }

    @Suppress("TooGenericExceptionCaught")
    private fun fromGeneric(genericAction: GenericAction): SageAction? = try {
        when (genericAction.type) {
            "VglsAction.InitWithId" -> SageAction.InitWithId(id = genericAction.argIdOne!!)
            else -> null
        }
    } catch (ex: NullPointerException) {
        hatchet.e("Invalid arguments to action: ${ex.message}")
        null
    }

    private fun toGeneric(action: SageAction): GenericAction = when (action) {
        is VglsAction.RefreshDbClicked -> GenericAction(type = "VglsAction.RefreshDbClicked")
        is VglsAction.DbSeeWhatsNewClicked -> GenericAction(type = "VglsAction.DbSeeWhatsNewClicked")
        is VglsAction.AppSeeWhatsNewClicked -> GenericAction(type = "VglsAction.AppSeeWhatsNewClicked")
        is SageAction.InitWithId -> GenericAction(type = "VglsAction.InitWithId", argIdOne = action.id)
        else -> throw IllegalArgumentException("Not a supported action: $action")
    }
}
