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
                val array = ArrayList<NewPayModel>()
                try {
                    //newPayDao.deleteAll()
                    val dbSales = newPayDao.getAll()
                    array.addAll(dbSales.map {
                        it.tipoConexionId = 2
                        it.origenId = 1
                        it.usuarioSesionId = 1
                        it.toModel()
                    })
                }
                catch (e: Exception){
                    MainActivity.mainDialog.value = true
                    MainActivity.mainDialogMsg.value = "Error ${e.message.toString()}"
                }

                array.addAll(newPay)
                val response = apiService.setPayment(payments = array)
                if (response.isSuccessful) { Result.success(Unit) }
                else {
                    MainActivity.mainDialog.value = true
                    MainActivity.mainDialogMsg.value = "Error ${response.code()}: ${response.errorBody()?.string()}"
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