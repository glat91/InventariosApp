package com.example.inventariosapp.domain.payment

import com.example.appgeneric.model.payment.NewPayModel
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.PayDao
import javax.inject.Inject

class PostPaymentUseCase @Inject constructor(
    private val payDao: PayDao,
    private val apiService: ApiService,
) {
    suspend operator fun invoke(
        newPay: List<NewPayModel>
    ): Result<Unit>{
        return try {
            val response = apiService.setPayment(payments = newPay)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}