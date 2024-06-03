package com.boukhari.cats.data.remote.model

data class OperationResponse(
    val id: String,
    val title: String,
    val amount: String,
    val date: Long,
    val category: String,
)