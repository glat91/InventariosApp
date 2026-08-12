package com.example.inventariosapp.domain.repository.payment

import com.example.appgeneric.model.payment.NewPayModel
import com.example.appgeneric.model.payment.toDb
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.local.dao.NewPayDao
import com.example.inventariosapp.session.SessionManager
import javax.inject.Inject

class PostPaymentRepositoryImp @Inject constructor(
    private val newPayDao: NewPayDao,
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(internetUse: Boolean, newPay: List<NewPayModel>): Result<Unit>{
        return try {
            if (internetUse){
                if (sessionManager.isSessionValid()){
                    val response = apiService.setPayment(payments = newPay)
                    if (response.isSuccessful) { Result.success(Unit) }
                    else {
                        MainActivity.mainDialogMsg.value = response.errorBody()?.string() ?: "Error en la peticion favor de intentar mas tarde"
                        MainActivity.mainDialog.value = true
                        Result.failure(Exception(response.errorBody()?.string() ?: "Error en la peticion favor de intentar mas tarde"))
                    }
                }
                else{
                    sessionManager.dialogLogin.value = true
                    Result.failure(Exception("Error sin session activa"))
                }
            }
            else{
                newPayDao.insertAll(newPay.map { it.toDb() })
                Result.success(Unit)
            }
        } catch (e: Exception) {
            MainActivity.mainDialogMsg.value = "Error 1001001"
            MainActivity.mainDialog.value = true
            Result.failure(e)
        }
    }
}