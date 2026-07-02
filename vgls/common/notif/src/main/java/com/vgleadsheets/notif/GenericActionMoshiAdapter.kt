package com.vgleadsheets.notif

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import net.sigmabeta.sage.appcomm.GenericAction

/**
 * Moshi adapter for SAGE's [GenericAction]. SAGE moved [GenericAction] from Moshi (`@JsonClass`) to
 * kotlinx.serialization (`@Serializable`) during its KMP migration, so it no longer carries a Moshi
 * adapter. VGLS's [Notif] persistence is still Moshi-based (`NotifState` -> `Notif` -> `GenericAction`),
 * so bridge it here via a codegen surrogate. Register with `Moshi.Builder().add(GenericActionMoshiAdapter)`.
 */
object GenericActionMoshiAdapter {
    @JsonClass(generateAdapter = true)
    data class Surrogate(
        val type: String,
        val argIdOne: Long? = null,
        val argIdTwo: Long? = null,
        val argString: String? = null,
    )

    @ToJson
    fun toJson(value: GenericAction): Surrogate =
        Surrogate(value.type, value.argIdOne, value.argIdTwo, value.argString)

    @FromJson
    fun fromJson(surrogate: Surrogate): GenericAction =
        GenericAction(surrogate.type, surrogate.argIdOne, surrogate.argIdTwo, surrogate.argString)
}
