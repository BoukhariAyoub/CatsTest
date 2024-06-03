package com.boukhari.cats.data.backup

import android.content.Context

import androidx.annotation.RawRes
import com.boukhari.cats.R
import com.boukhari.cats.data.remote.model.BankResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BankBackupServiceImpl(private val context: Context) : BankBackupService {

    override suspend fun fetchBanks(): List<BankResponse> {
        return readJsonFromRaw(R.raw.banks).fromJson()
    }

    private fun readJsonFromRaw(@RawRes resourceId: Int): String {
        return context.resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
    }

    private fun String.fromJson(): List<BankResponse> {
        val type = object : TypeToken<BanksWrapper>() {}.type
        val wrapper: BanksWrapper = Gson().fromJson(this, type)
        return wrapper.banks
    }

    data class BanksWrapper(
        val banks: List<BankResponse>
    )
}
