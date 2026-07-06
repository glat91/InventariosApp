package com.example.inventariosapp.domain.repository.sales

import android.util.Log
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.local.dao.SalesDao
import com.example.inventariosapp.local.entity.toDb
import com.example.inventariosapp.domain.model.error.ErrorModel
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.domain.model.sales.toDB
import com.example.inventariosapp.util.NetworkMonitor
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSalesInProcessRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val salesDao: SalesDao,
    private val networkMonitor: NetworkMonitor
) {
    suspend operator fun invoke(startDate: String, endDate: String, internetUse: Boolean): Pair<List<SalesModel>?, String?>{
        return if (networkMonitor.isConnected.value && MainActivity.internetBtn.value ) {
            fetchFromNetwork(startDate, endDate)
        }
        else { fetchFromLocal(startDate, endDate) }
    }

    private suspend fun fetchFromLocal(startDate: String, endDate: String): Pair<List<SalesModel>?, String?>{
        try {
            val sales = salesDao.getSalesBetween(startDate, endDate, 1)
            val entity = ArrayList(sales.map { it.toDb() })
            return Pair(entity, null)
        }
        catch (e: Exception){
            MainActivity.mainDialogMsg.value = e.toString()
            MainActivity.mainDialog.value = true
            return Pair(null, e.toString())
        }
    }

    private suspend fun fetchFromNetwork(startDate: String, endDate: String): Pair<List<SalesModel>?, String?>{
        val r = apiService.getPendingSales(fechaInicio = startDate, fechaFin = endDate)
        val response = try {
            if (r.isSuccessful) {
                try {
                    withContext(Dispatchers.IO){
                        val data = r.body()?.map { it.toDB() } ?: emptyList()
                        salesDao.insertAllSales(data)
                    }
                }
                catch (e: Exception){ }
                Pair(r.body(), null)
            }
            else {
                var error: ErrorModel
                val errorMsj = r.errorBody()?.string()
                error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                MainActivity.mainDialogMsg.value = "Error en la peticion favor de intentar mas tarde"
                MainActivity.mainDialog.value = true
                Pair(null, "Error en la peticion favor de intentar mas tarde")
            }
        }
        catch (e: Exception){
            MainActivity.mainDialogMsg.value = "Error 1001001"
            MainActivity.mainDialog.value = true
            Pair(null, "Error 1001001")
        }
        return response
    }
    /*
    suspend operator fun invoke(startDate: String, endDate: String, internetUse: Boolean): Pair<List<SalesModel>?, String?> {
        if (internetUse){
            val r = apiService.getPendingSales(fechaInicio = startDate, fechaFin = endDate)

            val response = try {
                if (r.isSuccessful) {
                    try {
                        withContext(Dispatchers.IO){
                            val data = r.body()?.map { it.toDB() } ?: emptyList()
                            salesDao.insertAllSales(data)
                        }
                    }
                    catch (e: Exception){ }
                    Pair(r.body(), null)
                }
                else {
                    var error: ErrorModel
                    val errorMsj = r.errorBody()?.string()
                    error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                    MainActivity.mainDialogMsg.value = error.MsgError.toString()
                    MainActivity.mainDialog.value = true
                    Pair(null, error.MsgError.toString())
                }
            }
            catch (e: Exception){
                MainActivity.mainDialogMsg.value = e.toString()
                MainActivity.mainDialog.value = true
                Pair(null, e.toString())
            }
            return response
        }
        else{
            try {
                val sales = salesDao.getSalesBetween(startDate, endDate, "1")
                val entity = ArrayList(sales.map { it.toDb() })
                return Pair(entity, null)
            }
            catch (e: Exception){
                MainActivity.mainDialogMsg.value = e.toString()
                MainActivity.mainDialog.value = true
                return Pair(null, e.toString())
            }
        }
    }
     */
}