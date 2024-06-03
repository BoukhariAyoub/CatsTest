package com.boukhari.cats.domain.model

data class Account(
    val id: String,
    val holder: String,
    val label: String,
    val balance: String,
    val order: Int,
    val role: Int?,
    val contractNumber: String?,
    val productCode: String?,
    val operations: List<Operation>
)

