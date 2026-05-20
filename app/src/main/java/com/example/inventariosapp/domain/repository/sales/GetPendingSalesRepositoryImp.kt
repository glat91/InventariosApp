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
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetPendingSalesRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val salesDao: SalesDao,
    private val networkMonitor: NetworkMonitor,
) {
    suspend operator fun invoke(estatusVentaIds: String, startDate: String, endDate: String, refresh: Boolean): Pair<List<SalesModel>?, String?> {
        return if (networkMonitor.isConnected.value && refresh) {
            fetchFromNetwork(estatusVentaIds, startDate, endDate)
        }
        else { fetchFromLocal(estatusVentaIds, startDate, endDate) }
    }

    private suspend fun fetchFromLocal(
        estatusVentaIds: String,
        startDate: String,
        endDate: String,
    ): Pair<List<SalesModel>?, String?> {
        Log.i("PendingSales___", "call db Sales")
        val sales = salesDao.getSalesBetween(startDate, endDate, estatusVentaIds)
        val entity = ArrayList(sales.map { it.toDb() })
        return Pair(entity, null)
    }
    private suspend fun fetchFromNetwork(
        estatusVentaIds: String,
        startDate: String,
        endDate: String,
    ): Pair<List<SalesModel>?, String?> {
        val r = apiService.getPendingSales(fechaInicio = startDate, fechaFin = endDate, estatusVentaIds = estatusVentaIds)
        if (r.isSuccessful) {
            val body = r.body()
            if (body != null) {
                try {
                    withContext(Dispatchers.IO) {
                        Log.i("PendingSales___", "update db Sales")
                        val data = body.map { it.toDB() }
                        val totalSales = salesDao.getAllSales()
                        Log.i("PendingSales___", "Save: ${totalSales.size < data.size}")
                        //salesDao.deleteAllSales()
                        salesDao.insertAllSales(data)
                        val total = salesDao.getAllSales()
                        Log.i("PendingSales___", "Total: ${total.size}")
                    }
                } catch (e: Exception) {
                    MainActivity.mainDialogMsg.value = "Error: ${e.message.toString()}"
                    MainActivity.mainDialog.value = true
                }
                return Pair(ArrayList(body), null)
            } else {
                return Pair(arrayListOf(), null)
            }
        }
        else {
            val errorMsj = r.errorBody()?.string()
            val error = try {
                Gson().fromJson(errorMsj, ErrorModel::class.java)
            }
            catch (e: Exception) {
                return Pair(null, "Error ${e.message.toString()}")
            }
            return Pair(null, "Error ${r.code()}: ${error}")
        }
    }
    /*
    suspend operator fun invoke(
        estatusVentaIds: String,
        startDate: String,
        endDate: String,
        refresh: Boolean
    ): Pair<ArrayList<SalesModel>?, String?> {
        return try {
            if (refresh) {
                val r = apiService.getPendingSales(fechaInicio = startDate, fechaFin = endDate, estatusVentaIds = estatusVentaIds)
                if (r.isSuccessful) {
                    val body = r.body()
                    if (body != null) {
                        try {
                            withContext(Dispatchers.IO) {
                                Log.i("Sales___", "update db Sales")
                                val data = body.map { it.toDB() }
                                val totalSales = salesDao.getAllSales()
                                Log.i("Sales___", "Save: ${totalSales.size < data.size}")
                                //salesDao.deleteAllSales()
                                salesDao.insertAllSales(data)
                                val total = salesDao.getAllSales()
                                Log.i("Sales___", "Total: ${total.size}")
                            }
                        }
                        catch (e: Exception) {
                            MainActivity.mainDialogMsg.value = "Error: ${e.message.toString()}"
                            MainActivity.mainDialog.value = true
                        }
                        Pair(ArrayList(body), null)
                    }
                    else { Pair(arrayListOf(), null) }
                }
                else {
                    val errorMsj = r.errorBody()?.string()
                    val error = try {
                        Gson().fromJson(errorMsj, ErrorModel::class.java)
                    }
                    catch (e: Exception) {
                        Pair(null, "Error ${e.message.toString()}")
                    }
                    Pair(null, "Error ${r.code()}: ${error}")
                }
            }
            else {
                Log.i("Sales___", "call db Sales")
                val sales = salesDao.getSalesBetween(startDate, endDate, estatusVentaIds)
                val entity = ArrayList(sales.map { it.toDb() })
                Pair(entity, null)
            }
        } catch (e: Exception) {
            Log.e("Sales___", "Error in invoke: ${e.message}")
            MainActivity.mainDialogMsg.value = e.message.toString()
            MainActivity.mainDialog.value = true
            Pair(null, e.message.toString())
        }
    }
     */
}