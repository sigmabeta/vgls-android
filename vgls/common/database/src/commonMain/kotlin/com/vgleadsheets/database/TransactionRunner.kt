package com.vgleadsheets.database

interface TransactionRunner {
    suspend fun inTransaction(action: suspend () -> Unit)
}
