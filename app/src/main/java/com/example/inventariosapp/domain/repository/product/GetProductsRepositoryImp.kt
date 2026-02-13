package com.example.inventariosapp.domain.repository.product

import android.util.Log
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.ProductDao
import com.example.inventariosapp.database.entity.toModel
import com.example.inventariosapp.domain.model.error.ErrorModel
import com.example.inventariosapp.domain.model.product.ProductsResponseModel
import com.example.inventariosapp.domain.model.product.toDb
import com.google.gson.Gson
import javax.inject.Inject

class GetProductsRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val productDao: ProductDao,
) {
    suspend operator fun invoke(internetUse: Boolean): Pair<List<ProductsResponseModel>?, String?> {
        if (internetUse) {
            val service = apiService.getProducts()
            val response = try {
                if (service.isSuccessful) {
                    Log.i("Products___", "update db Products")
                    productDao.deleteAllProducts()
                    val data = service.body()!!.map { it.toDb() }
                    productDao.insertAll(data)
                    Pair(service.body(), null)
                }
                else {
                    var error: ErrorModel
                    val errorMsj = service.errorBody()?.string()
                    error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                    MainActivity.mainDialogMsg.value = error.MsgError?.errors.toString()
                    MainActivity.mainDialog.value = true
                    Pair(null, error.MsgError?.errors.toString())
                }
            }
            catch (e: Exception) {
                MainActivity.mainDialogMsg.value = e.toString()
                MainActivity.mainDialog.value = true
                Pair(null, e.message.toString())
            }
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
                MainActivity.mainDialogMsg.value = e.toString()
                MainActivity.mainDialog.value = true
                return Pair(null, e.message ?: "Error desconocido")
            }
        }
    }
}