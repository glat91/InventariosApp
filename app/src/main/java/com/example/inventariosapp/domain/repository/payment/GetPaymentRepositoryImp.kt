package com.example.inventariosapp.domain.repository.payment

import android.content.Context
import android.util.Log
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.PayDao
import com.example.inventariosapp.database.entity.toDB
import com.example.inventariosapp.model.error.ErrorModel
import com.example.inventariosapp.model.payment.PayModel
import com.example.inventariosapp.model.payment.toDB
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPaymentRepositoryImp @Inject constructor(
    private val payDao: PayDao,
    private val apiService: ApiService,
    @ApplicationContext val cnx: Context
) {
    suspend operator fun invoke(ventaID: String, refresh: Boolean): Pair<ArrayList<PayModel>?, ErrorModel?>{
        if (refresh || MainActivity.internetBtn.value){
            val r = apiService.getPayment(ventaID)
            val response = try {
                if (r.isSuccessful) {
                    try {
                        withContext(Dispatchers.IO){
                            Log.i("Pay___", "update db Pay")
                            val data = r.body()?.map { it.toDB() } ?: emptyList()
                            payDao.insertAll(data)
                        }
                    }
                    catch (e: Exception){ }
                    Pair(r.body(), null)
                }
                else {
                    var error: ErrorModel
                    val errorMsj = r.errorBody()?.string()
                    error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                    Pair(null, error)
                }
            }
            catch (e: Exception){ Pair(null, null) }
            return response as Pair<ArrayList<PayModel>?, ErrorModel?>
        }
        else{
            try {
                Log.i("Pay___", "use db Pay")
                val pay = payDao.getPaymentsByVentaId(ventaID.toInt())
                val entity = ArrayList(pay.map { it.toDB() })
                return Pair(entity, null)
            }
            catch (e: Exception){
                return Pair(
                    null,
                    ErrorModel(error("Error en Base de Datos, favor de contactar a Administracion"))
                )
            }
        }
    }
}