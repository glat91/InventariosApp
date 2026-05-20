package com.example.inventariosapp.domain.repository.product

import android.util.Log
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.local.dao.InventoryDao
import com.example.inventariosapp.local.entity.toModel
import com.example.inventariosapp.domain.model.error.ErrorModel
import com.example.inventariosapp.domain.model.product.InventarioRseponeModel
import com.example.inventariosapp.domain.model.product.toDb
import com.example.inventariosapp.util.NetworkMonitor
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetInventarioRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val inventoryDao: InventoryDao,
    private val networkMonitor: NetworkMonitor
) {
    suspend operator fun invoke(): Pair<List<InventarioRseponeModel>?, ErrorModel?> {
        return if (networkMonitor.isConnected.value && MainActivity.internetBtn.value ) { fetchFromNetwork() }
        else { fetchFromLocal() }
    }
    private suspend fun fetchFromLocal(): Pair<List<InventarioRseponeModel>?, ErrorModel?> {
        try {
            val inventory = inventoryDao.getAll().map { it.toModel() }
            return Pair(inventory, null)
        }
        catch (e: Exception){
            return Pair(null, ErrorModel(error(e.message.toString())))
        }
    }
    private suspend fun fetchFromNetwork(): Pair<List<InventarioRseponeModel>?, ErrorModel?> {
        val service = apiService.getInventario()
        val response = try {
            if (service.isSuccessful) {
                val r = service.body()?.map { it.toDb() } ?: emptyList()
                withContext(Dispatchers.IO) {
                    Log.i("Inventory___", "update db Inventory")
                    inventoryDao.insertAll(r)
                }
                Pair(service.body(), null)
            }
            else {
                var error: ErrorModel
                val errorMsj = service.errorBody()?.string()
                error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                Pair(null, error)
            }
        } catch (e: Exception) {
            MainActivity.mainDialogMsg.value = e.toString()
            Pair(null, ErrorModel(error("Error, favor de revisar su conexion a internet")))
        }
        return response

    }
    /*
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
                MainActivity.mainDialogMsg.value = e.toString()
                Pair(null, ErrorModel(error("Error, favor de revisar su conexion a internet")))
            }
            return response
        }
        else{
            Log.i("Inventory___", "call db Inventory")
            try {
                val inventory = inventoryDao.getAll().map { it.toModel() }
                return Pair(inventory, null)
            }
            catch (e: Exception){
                return Pair(null, ErrorModel(error(e.message.toString())))
            }
        }
    }
     */
}