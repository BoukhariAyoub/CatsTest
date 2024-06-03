package com.boukhari.cats.data.backup

import com.boukhari.cats.data.remote.model.BankResponse

interface BankBackupService  {

    suspend fun fetchBanks(): List<BankResponse>
}
