package com.boukhari.cats.data.remote.model

data class BankResponse(
    val name: String,
    val isCA: Int,
    val accounts: List<AccountResponse>
)
