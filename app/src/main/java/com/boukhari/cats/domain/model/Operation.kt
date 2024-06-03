package com.boukhari.cats.domain.model

data class Operation(
    val id: String,
    val title: String,
    val amount: String,
    val date: Long,
    val category: String,
)
