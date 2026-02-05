package com.example.appgeneric.domain.sales

import android.util.Log
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.SalesDao
import com.example.inventariosapp.database.entity.toDb
import com.example.inventariosapp.model.error.ErrorModel
import com.example.inventariosapp.model.sales.SalesModel
import com.example.inventariosapp.model.sales.toDB
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetPendingSalesRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val salesDao: SalesDao,
) {
    suspend operator fun invoke(startDate: String, endDate: String, refresh: Boolean): Pair<ArrayList<SalesModel>?, ErrorModel?> {
        return try {
            if (refresh || MainActivity.internetBtn.value) {
                val r = apiService.getPendingSales(fechaInicio = startDate, fechaFin = endDate)
                if (r.isSuccessful) {
                    val body = r.body()
                    if (body != null) {
                        try {
                            withContext(Dispatchers.IO) {
                                Log.i("Sales___", "update db Sales")
                                val data = body.map { it.toDB() }
                                salesDao.insertAllSales(data)
                                if (refresh) MainActivity.mainDialog.value = true
                            }
                        } catch (e: Exception) {
                            Log.e("Sales___", "Error saving to DB: ${e.message}")
                        }
                        Pair(ArrayList(body), null)
                    } else {
                        Pair(arrayListOf<SalesModel>(), null)
                    }
                } else {
                    val errorMsj = r.errorBody()?.string()
                    val error = try {
                        Gson().fromJson(errorMsj, ErrorModel::class.java)
                    } catch (e: Exception) {
                        null
                    }
                    Pair(null, error)
                }
            } else {
                Log.i("Sales___", "call db Sales")
                val sales = salesDao.getSalesBetween(startDate, endDate)
                val entity = ArrayList(sales.map { it.toDb() })
                Pair(entity, null)
            }
        } catch (e: Exception) {
            Log.e("Sales___", "Error in invoke: ${e.message}")
            MainActivity.mainDialogMsg.value = "Error en Base de Datos, favor de contactar a Administracion"
            Pair(null, null)
        }
    }
}