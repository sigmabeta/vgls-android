package com.vgleadsheets.database

interface TransactionRunner {
    fun inTransaction(action: () -> Unit)
}
