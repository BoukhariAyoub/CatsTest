package com.boukhari.cats.data.repo

import com.boukhari.cats.data.remote.model.AccountResponse
import com.boukhari.cats.data.remote.model.BankResponse
import com.boukhari.cats.data.remote.model.OperationResponse
import com.boukhari.cats.domain.model.Account
import com.boukhari.cats.domain.model.Bank
import com.boukhari.cats.domain.model.Operation

fun BankResponse.toDomainModel(): Bank {
    return Bank(
        name = name,
        isCA = isCA == 1,
        accounts = accounts.map { it.toDomainModel() }
    )
}

fun AccountResponse.toDomainModel(): Account {
    return Account(
        id = id,
        holder = holder,
        balance = balance,
        order = order,
        role = role,
        contractNumber = contractNumber,
        label = label,
        productCode = productCode,
        operations = operations.map { it.toDomainModel() }
    )
}

fun OperationResponse.toDomainModel(): Operation {
    return Operation(
        id = id,
        title = title,
        amount = amount,
        date = date,
        category = category,
    )
}