package com.example.inventariosapp.domain.repository.sales

import android.util.Log
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.local.dao.SalesDao
import com.example.inventariosapp.local.entity.toDb
import com.example.inventariosapp.domain.model.error.ErrorModel
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.domain.model.sales.toDB
import com.example.inventariosapp.util.Helpers
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetPendingSalesRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val salesDao: SalesDao,
) {
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
}