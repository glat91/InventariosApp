package com.example.inventariosapp.domain.repository.sales

import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.SalesDao
import com.example.inventariosapp.database.entity.toDb
import com.example.inventariosapp.domain.model.error.ErrorModel
import com.example.inventariosapp.domain.model.sales.SalesModel
import com.example.inventariosapp.domain.model.sales.toDB
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSalesInProcessRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val salesDao: SalesDao,
) {
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
                val sales = salesDao.getSalesBetween(startDate, endDate)
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
}