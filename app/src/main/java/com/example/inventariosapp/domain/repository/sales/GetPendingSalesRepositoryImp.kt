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
        if (refresh || MainActivity.internetBtn.value){
            val r = apiService.getPendingSales(fechaInicio = startDate, fechaFin = endDate)
            val response = try {
                if (r.isSuccessful) {
                    try {
                        withContext(Dispatchers.IO){
                            Log.i("Sales___", "update db Sales")
                            val data = r.body()?.map { it.toDB() } ?: emptyList()
                            salesDao.insertAllSales(data)
                            if (refresh) MainActivity.mainDialog.value = true
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
            return response as Pair<ArrayList<SalesModel>?, ErrorModel?>
        }
        else{
            try {
                Log.i("Sales___", "call db Sales")
                val sales = salesDao.getSalesBetween(startDate, endDate)
                val entity = ArrayList(sales.map { it.toDb() })
                return Pair(entity, null)
            }
            catch (e: Exception){
                MainActivity.mainDialogMsg.value = e.toString()
                return Pair(
                    null,
                    ErrorModel(error("Error en Base de Datos, favor de contactar a Administracion"))
                )
            }
        }
    }
}