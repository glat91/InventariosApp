package com.example.inventariosapp.domain.repository.payment

import android.content.Context
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.PayDao
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeletePaymentRepositoryImp @Inject constructor(
    private val payDao: PayDao,
    private val apiService: ApiService,
    @ApplicationContext val cnx: Context
) {
    suspend operator fun invoke(pagoId: Int): Result<Unit>{
        return try {
            val r = apiService.deletePayment(pagoId)
            if (r.isSuccessful){ Result.success(Unit) }
            else{ Result.failure(Exception("Error ${r.code()}: ${r.errorBody()?.string()}")) }
        }
        catch (e: Exception) {
            MainActivity.mainDialogMsg.value = e.message.toString()
            MainActivity.mainDialog.value = true
            Result.failure(e)
        }
    }
}