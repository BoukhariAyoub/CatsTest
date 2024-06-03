package com.boukhari.cats.presentation

object Routes {
    const val BANK_LIST_SCREEN = "bankList"
    const val OPERATION_LIST_SCREEN = "accountDetailsScreen/{accountId}"

    fun operationListRoute(accountId: String) = "accountDetailsScreen/$accountId"
}
