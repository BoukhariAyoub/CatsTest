package com.boukhari.cats.data.remote.model

data class AccountResponse(
    val id: String,
    val holder: String,
    val balance: String,
    val label: String,
    val order: Int ,
    val role: Int?= null,
    val contractNumber: String?= null,
    val productCode: String?= null,
    val operations: List<OperationResponse>
)