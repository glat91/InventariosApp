package com.example.inventariosapp.domain.product

import android.content.Context
import android.util.Log
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.InventoryDao
import com.example.inventariosapp.database.entity.toModel
import com.example.inventariosapp.model.error.ErrorModel
import com.example.inventariosapp.model.product.InventarioRseponeModel
import com.example.inventariosapp.model.product.toDb
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetInventarioUseCase @Inject constructor(
    private val apiService: ApiService,
    private val inventoryDao: InventoryDao,
    @ApplicationContext var cnx: Context,
) {
    suspend operator fun invoke(refresh: Boolean): Pair<List<InventarioRseponeModel>?, ErrorModel?> {
        if(refresh || MainActivity.internetBtn.value) {
            val service = apiService.getInventario()
            val response = try {
                if (service.isSuccessful) {
                    val r = service.body()?.map { it.toDb() } ?: emptyList()
                    withContext(Dispatchers.IO) {
                        Log.i("Inventory___", "update db Inventory")
                        val inventory = inventoryDao.insertAll(r)
                    }
                    Pair(service.body(), null)
                } else {
                    var error: ErrorModel
                    val errorMsj = service.errorBody()?.string()
                    error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                    Pair(null, error)
                }
            } catch (e: Exception) {
                Pair(null, ErrorModel(error("Error, favor de revisar su conexion a internet")))
            }
            return response
        }
        else{
            Log.i("Inventory___", "call db Inventory")
            try {
                MainActivity.mainDialog.value = true
                val inventory = inventoryDao.getAll().map { it.toModel() }
                return Pair(inventory, null)
            }
            catch (e: Exception){
                return Pair(null, ErrorModel(error("Error en Base de Datos, favor de contactar a Administracion")))
            }
        }
    }
}