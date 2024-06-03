package com.boukhari.cats.domain.usecase

import com.boukhari.cats.domain.BankRepository
import com.boukhari.cats.domain.DataFetchException
import com.boukhari.cats.domain.model.Bank
import com.boukhari.cats.domain.model.BankType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetBanksUseCase(private val repository: BankRepository) {

    fun getBanksFlow(): Flow<Map<BankType, List<Bank>>> {
        return repository.getBanksFlow()
            .map { banks ->
                val creditAgricoleBanks = banks.filter { it.isCA }.sortedBy { it.name }
                val otherBanks = banks.filter { !it.isCA }.sortedBy { it.name }
                mapOf(
                    BankType.CA to creditAgricoleBanks,
                    BankType.OTHER to otherBanks
                )
            }
            .catch { e ->
                throw DataFetchException("Error while fetching banks : ${e.message}")
            }
    }

    suspend fun fetchBanks() {
        try {
            repository.fetchBanks()
        } catch (e: Exception) {
            throw DataFetchException("Error while fetching banks : ${e.message}")
        }
    }
}
