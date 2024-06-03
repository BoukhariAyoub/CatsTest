package com.boukhari.cats.data.local.cache

import com.boukhari.cats.data.remote.model.AccountResponse
import com.boukhari.cats.data.remote.model.BankResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class BankCacheDataSourceTest {

    @InjectMocks
    private lateinit var bankCacheDataSource: BankCacheDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `getBanks returns empty list initially`() = runTest {
        // When
        val result = bankCacheDataSource.getBanks().first()

        // Then
        assertEquals(emptyList<BankResponse>(), result)
    }

    @Test
    fun `saveBanks updates the bank flow`() = runTest {
        // Given
        val banks = listOf(
            BankResponse("Bank1", 1, listOf()),
            BankResponse("Bank2", 0, listOf())
        )
        bankCacheDataSource.saveBanks(banks)

        // When
        val result = bankCacheDataSource.getBanks().first()

        // Then
        assertEquals(banks, result)
    }

    @Test
    fun `getAccountById throws exception when account not found`() = runTest {
        val exception = assertThrows(IllegalStateException::class.java) {
            runBlocking {
                bankCacheDataSource.getAccountById("3").first()
            }
        }

        assertEquals("Account not found", exception.message)
    }

    @Test
    fun `getAccountById returns account when found`() = runTest {
        // Given
        val accountResponse =
            AccountResponse("1", "Holder1", "1000", "Label1", 1, operations = listOf())
        val banks = listOf(
            BankResponse("Bank1", 1, listOf(accountResponse)),
            BankResponse(
                "Bank2",
                0,
                listOf(AccountResponse("2", "Holder2", "2000", "Label2", 2, operations = listOf()))
            )
        )
        bankCacheDataSource.saveBanks(banks)

        // When
        val result = bankCacheDataSource.getAccountById("1").first()

        // Then
        assertEquals(accountResponse, result)
    }
}
