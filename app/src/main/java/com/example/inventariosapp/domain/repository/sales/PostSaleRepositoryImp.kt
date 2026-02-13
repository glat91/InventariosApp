package com.example.inventariosapp.domain.repository.sales

import android.util.Log
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.PostSalesDao
import com.example.inventariosapp.model.error.ErrorModel
import com.example.inventariosapp.model.sales.PostSalesModel
import com.example.inventariosapp.model.sales.toEntity
import com.example.inventariosapp.session.SessionManager
import com.google.gson.Gson
import java.io.IOException
import javax.inject.Inject

class PostSaleRepositoryImp @Inject constructor(
    private val sessionManager: SessionManager,
    private val apiService: ApiService,
    private val newSales: PostSalesDao,
){
    suspend operator fun invoke(sales: List<PostSalesModel>?, internetUse: Boolean): Pair<Unit?, String?> {
        if (internetUse){
            if (sessionManager.isSessionValid()) {
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
                        MainActivity.mainDialogMsg.value = error.MsgError?.errors.toString()
                        MainActivity.mainDialog.value = true

                        Pair(null, error.MsgError?.errors.toString())
                    }
                }
                catch (e: Exception) {
                    MainActivity.mainDialogMsg.value = e.toString()
                    MainActivity.mainDialog.value = true
                    Pair(null, e.message ?: "Error inesperado")
                }
            }
            else {
                MainActivity.mainDialogMsg.value ="Sesión expirada, favor de iniciar sesión"
                MainActivity.mainDialog.value = true
                return Pair(null, "Sesión expirada, favor de iniciar sesión")
            }
        }
        else {
            return try {
                Log.i("PostSales___", "Guardando ventas en DB (offline)")
                sales?.forEach {
                    val saleEntity = it.toEntity()
                    val productsEntity = it.ventaProductos.map { product ->
                        product.toEntity(parentId = saleEntity.id)
                    }
                    newSales.insertSaleWithProducts(
                        sale = saleEntity,
                        products = productsEntity
                    )
                }
                Pair(Unit, null)
            }
            catch (e: Exception) {
                MainActivity.mainDialogMsg.value = e.toString()
                MainActivity.mainDialog.value = true
                Pair(null, e.message.toString())
            }
        }
    }
}