package com.example.inventariosapp.domain.repository.product

import android.util.Log
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.ProductDao
import com.example.inventariosapp.database.entity.toModel
import com.example.inventariosapp.model.error.ErrorModel
import com.example.inventariosapp.model.product.ProductsResponseModel
import com.example.inventariosapp.model.product.toDb
import com.google.gson.Gson
import javax.inject.Inject

class GetProductsRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val productDao: ProductDao,
) {
    suspend operator fun invoke(refresh: Boolean): Pair<List<ProductsResponseModel>?, ErrorModel?> {
        if (refresh || MainActivity.internetBtn.value) {
            val service = apiService.getProducts()
            val response = try {
                if (service.isSuccessful) {
                    Log.i("Products___", "update db Products")
                    productDao.deleteAllProducts()
                    val data = service.body()!!.map { it.toDb() }
                    productDao.insertAll(data)
                    if (refresh){
                        MainActivity.mainDialog.value = true
                    }
                    Pair(service.body(), null)
                }
                else {
                    var error: ErrorModel
                    val errorMsj = service.errorBody()?.string()
                    error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                    Pair(null, error)
                }
            }
            catch (e: Exception) { Pair(null, ErrorModel(error("Error, favor de revisar su conexion a internet"))) }
            return response
        }
        else{
            try {
                Log.i("Products___", "call db Products")
                val products = productDao.getAllProducts()
                val entity = products.map { it.toModel() }
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