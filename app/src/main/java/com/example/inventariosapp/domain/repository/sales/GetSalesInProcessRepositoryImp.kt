package com.example.inventariosapp.domain.repository.sales

import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.SalesDao
import com.example.inventariosapp.database.entity.toDb
import com.example.inventariosapp.model.error.ErrorModel
import com.example.inventariosapp.model.sales.SalesModel
import com.example.inventariosapp.model.sales.toDB
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSalesInProcessRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val salesDao: SalesDao,
) {
    suspend operator fun invoke(startDate: String, endDate: String, refresh: Boolean): Pair<List<SalesModel>?, ErrorModel?> {
        if (refresh || MainActivity.internetBtn.value){
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
                    Pair(null, error)
                }
            }
            catch (e: Exception){ Pair(null, null) }
            return response
        }
        else{
            try {
                val sales = salesDao.getSalesBetween(startDate, endDate)
                val entity = ArrayList(sales.map { it.toDb() })
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