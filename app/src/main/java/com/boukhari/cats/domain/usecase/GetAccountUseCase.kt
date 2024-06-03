package com.boukhari.cats.domain.usecase

import com.boukhari.cats.domain.AccountNotFoundException
import com.boukhari.cats.domain.BankRepository
import com.boukhari.cats.domain.model.Account
import com.boukhari.cats.domain.model.Operation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetAccountUseCase(private val repository: BankRepository) {

    fun getAccountById(accountId: String): Flow<Account> {
        return repository.getAccount(accountId)
            .map { account ->
                account.copy(
                    operations = account.operations.sortedWith(
                        compareByDescending<Operation> { it.date }
                            .thenBy { it.title }
                    )
                )
            }
            .catch { e ->
                throw AccountNotFoundException("the account with id $accountId was not found : ${e.message}")
            }
    }
}


