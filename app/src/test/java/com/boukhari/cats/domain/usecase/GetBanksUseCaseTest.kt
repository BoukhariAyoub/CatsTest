package com.boukhari.cats.domain.usecase

import com.boukhari.cats.domain.BankRepository
import com.boukhari.cats.domain.model.Bank
import com.boukhari.cats.domain.model.BankType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class GetBanksUseCaseTest {

    @Mock
    private lateinit var repository: BankRepository

    @InjectMocks
    private lateinit var getBanksUseCase: GetBanksUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `getBanksFlow returns sorted banks by type and name`() = runTest {
        // Given
        val creditAgricoleBank = Bank(name = "Credit Agricole", isCA = true, accounts = emptyList())
        val otherBank = Bank(name = "Other Bank", isCA = false, accounts = emptyList())
        val banks = listOf(otherBank, creditAgricoleBank)
        `when`(repository.getBanksFlow()).thenReturn(flow { emit(banks) })

        // When
        val result = getBanksUseCase.getBanksFlow().toList()

        // Then
        val expected = mapOf(
            BankType.CA to listOf(creditAgricoleBank),
            BankType.OTHER to listOf(otherBank)
        )
        assertEquals(expected, result.first())
        verify(repository).getBanksFlow()
    }

    @Test
    fun `fetchBanks calls repository fetchBanks`() = runTest {
        // When
        getBanksUseCase.fetchBanks()

        // Then
        verify(repository).fetchBanks()
    }
}
