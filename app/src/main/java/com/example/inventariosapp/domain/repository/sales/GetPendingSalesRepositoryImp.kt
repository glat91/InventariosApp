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
        val estatusInt = estatusVentaIds.toIntOrNull() ?: 0

        // Log para depuración
        val allSales = salesDao.getSalesByEstatusDebug(2)
        Log.d("DEBUG_LOCAL", "Total en base de datos: ${allSales.size}")
        allSales.forEach {
            Log.d("DEBUG_LOCAL", it.toString())
        }

        val sales = salesDao.getSalesBetween(startDate, endDate, estatusInt)
        Log.d("DEBUG_LOCAL", "Querying: start=$startDate, end=$endDate, status=$estatusInt")
        Log.d("DEBUG_LOCAL", "Result count: ${sales.size}")

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
                    Log.i("PendingSales___", "body: ${body.size}")
                    val data = body.map { it.toDB() }
                    Log.i("PendingSales___", "body: ${data.size}")
                    salesDao.deleteAllSales()
                    salesDao.insertAllSales(data)
                } catch (e: Exception) {
                    MainActivity.mainDialogMsg.value = "Error 1001001"
                    MainActivity.mainDialog.value = true
                }
                return Pair(ArrayList(body), null)
            } else {
                return Pair(arrayListOf(), null)
            }
        }
        else {
            return Pair(null, "Error en red")
        }
    }
}
