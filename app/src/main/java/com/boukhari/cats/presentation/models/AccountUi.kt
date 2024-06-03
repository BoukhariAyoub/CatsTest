package com.boukhari.cats.presentation.models

data class AccountUi(
    val id: String,
    val label: String,
    val balance: String,
    val operations : List<OperationUi>
)