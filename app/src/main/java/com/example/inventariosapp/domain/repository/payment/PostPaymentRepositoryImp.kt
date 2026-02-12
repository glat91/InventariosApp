package com.example.inventariosapp.domain.repository.payment

import com.example.appgeneric.model.payment.NewPayModel
import com.example.appgeneric.model.payment.toDb
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.NewPayDao
import com.example.inventariosapp.database.entity.toModel
import javax.inject.Inject

class PostPaymentRepositoryImp @Inject constructor(
    private val newPayDao: NewPayDao,
    private val apiService: ApiService,
) {
    suspend operator fun invoke(internetUse: Boolean, newPay: List<NewPayModel>): Result<Unit>{
        return try {
            if (internetUse){
                val response = apiService.setPayment(payments = newPay)
                if (response.isSuccessful) { Result.success(Unit) }
                else {
                    MainActivity.mainDialogMsg.value = "Error ${response.code()}: ${response.errorBody()?.string()}"
                    MainActivity.mainDialog.value = true
                    Result.failure(Exception("Error ${response.code()}: ${response.errorBody()?.string()}"))
                }
            }
            else{
                newPayDao.insertAll(newPay.map { it.toDb() })
                Result.success(Unit)
            }
        } catch (e: Exception) {
            MainActivity.mainDialogMsg.value = e.message ?: "Error desconocido"
            MainActivity.mainDialog.value = true
            Result.failure(e)
        }
    }
}