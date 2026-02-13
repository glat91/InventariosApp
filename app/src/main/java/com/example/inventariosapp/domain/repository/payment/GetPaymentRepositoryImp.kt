package com.example.inventariosapp.domain.repository.payment

import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.local.dao.PayDao
import com.example.inventariosapp.local.entity.toDB
import com.example.inventariosapp.domain.model.error.ErrorModel
import com.example.inventariosapp.domain.model.payment.PayModel
import com.example.inventariosapp.domain.model.payment.toDB
import com.google.gson.Gson
import javax.inject.Inject

class GetPaymentRepositoryImp @Inject constructor(
    private val payDao: PayDao,
    private val apiService: ApiService,
) {
    suspend operator fun invoke(ventaID: String, internetUse: Boolean): Pair<List<PayModel>?, String?>{
        return getPayments(ventaID, internetUse = internetUse)
    }

    suspend fun getPayments(
        ventaID: String,
        internetUse: Boolean
    ): Pair<List<PayModel>?, String?>{
        return if (internetUse) { fetchFromApi(ventaID) }
        else { fetchFromDb(ventaID) }
    }

    private suspend fun fetchFromApi(ventaID: String): Pair<List<PayModel>?, String?>{
        return try {
            val response = apiService.getPayment(ventaID)

            if (response.isSuccessful) {
                val body = response.body().orEmpty()
                payDao.insertAll(body.map { it.toDB() })
                Pair(body, null)
            }
            else {
                val error = response.errorBody()?.string()?.let {
                    Gson().fromJson(it, ErrorModel::class.java)
                }
                Pair(null, error?.MsgError?.errors.toString())
            }
        }
        catch (e: Exception) { Pair(null, e.message?: "Error desconocido") }
    }

    private suspend fun fetchFromDb(ventaID: String): Pair<List<PayModel>?, String?>{
        return try {
            val pay = payDao.getPaymentsByVentaId(ventaID.toInt())
            Pair(pay.map { it.toDB() }, null)
        }
        catch (e: Exception) { Pair(null, e.message.toString()) }
    }
}