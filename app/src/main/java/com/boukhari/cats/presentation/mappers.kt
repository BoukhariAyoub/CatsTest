package com.boukhari.cats.presentation

import com.boukhari.cats.domain.model.Account
import com.boukhari.cats.domain.model.Bank
import com.boukhari.cats.domain.model.Operation
import com.boukhari.cats.presentation.models.AccountUi
import com.boukhari.cats.presentation.models.BankUi
import com.boukhari.cats.presentation.models.OperationUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Bank.toUi(): BankUi {
    return BankUi(
        name = this.name,
        accounts = this.accounts.map { account ->
            account.toUi()
        }
    )
}

fun Account.toUi() = AccountUi(
    id = this.id,
    label = this.label,
    balance = this.balance,
    operations = this.operations.map { operation ->
        operation.toUi()
    }
)

fun Operation.toUi() = OperationUi(
    title = this.title,
    amount = this.amount,
    date = this.date.toFormattedDate(),
)

fun Long.toFormattedDate(): String {
    val date = Date(this * 1000) // Convert seconds to milliseconds
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}