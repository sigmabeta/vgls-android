package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Transaction
import com.vgleadsheets.database.TransactionRunner

@Dao
interface TransactionDao : TransactionRunner {
    @Transaction
    override suspend fun inTransaction(action: suspend () -> Unit) {
        action()
    }
}
