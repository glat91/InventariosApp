package com.example.inventariosapp.domain.repository.sales

import android.content.Context
import android.util.Log
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.PostSalesDao
import com.example.inventariosapp.model.error.ErrorModel
import com.example.inventariosapp.model.sales.PostSalesModel
import com.example.inventariosapp.model.sales.toEntity
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class PostSaleRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val newSales: PostSalesDao,
    @ApplicationContext val cnx: Context
){
    suspend operator fun invoke(sales: List<PostSalesModel>?, updateSales: Boolean): Pair<Unit?, ErrorModel?> {
        if (updateSales || MainActivity.internetBtn.value) {

            val payload = ArrayList(sales ?: emptyList())

            return try {
                val response = apiService.postSale(payload)

                if (response.isSuccessful) {
                    Log.i("PostSales___", "Sales enviadas correctamente")
                    Pair(Unit, null)
                } else {
                    val errorJson = response.errorBody()?.string()
                    val error = errorJson?.let {
                        Gson().fromJson(it, ErrorModel::class.java)
                    } ?: ErrorModel(error("Error desconocido del servidor"))

                    Pair(null, error)
                }

            } catch (e: IOException) {
                Pair(null, ErrorModel(error("Sin conexión a internet")))

            } catch (e: Exception) {
                Pair(null, ErrorModel(error(e.message ?: "Error inesperado")))
            }

        } else {
            // OFFLINE
            return try {
                Log.i("PostSales___", "Guardando ventas en DB (offline)")
                sales?.forEach { newSales.insertSale(it.toEntity()) }
                Pair(Unit, null)
            }
            catch (e: Exception) {
                MainActivity.mainDialogMsg.value = e.toString()
                Pair(
                    null,
                    ErrorModel(error("Error en Base de Datos, contacte a Administración"))
                )
            }
        }
    }
}