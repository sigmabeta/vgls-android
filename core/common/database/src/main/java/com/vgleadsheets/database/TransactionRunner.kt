package com.vgleadsheets.database

interface TransactionRunner {
    suspend fun runInTransaction(action: suspend () -> Unit)
}
