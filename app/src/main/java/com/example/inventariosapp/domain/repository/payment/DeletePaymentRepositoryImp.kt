package com.example.inventariosapp.domain.repository.payment

import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.PayDao
import com.example.inventariosapp.session.SessionManager
import javax.inject.Inject

class DeletePaymentRepositoryImp @Inject constructor(
    private val payDao: PayDao,
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(internetUse: Boolean, pagoId: Int): Result<Unit>{
        return try {
            if (internetUse) {
                if (sessionManager.isSessionValid()){
                    val response = apiService.deletePayment(pagoId)
                    if (response.isSuccessful) { Result.success(Unit)
                    } else {
                        MainActivity.mainDialog.value = true
                        MainActivity.mainDialogMsg.value = "Error ${response.code()}: ${response.errorBody()?.string()}"
                        Result.failure(Exception("Error ${response.code()}: ${response.errorBody()?.string()}"))
                    }
                }
                else{
                    sessionManager.dialogLogin.value = true
                    Result.failure(Exception("Error sin session activa"))
                }
            }
            else{
                payDao.deleteById(pagoId)
                Result.success(Unit)
            }
        }
        catch (e: Exception) {
            MainActivity.mainDialogMsg.value = e.message.toString()
            MainActivity.mainDialog.value = true
            Result.failure(e)
        }
    }
}