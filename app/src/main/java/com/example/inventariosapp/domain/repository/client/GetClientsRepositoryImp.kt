package com.example.inventariosapp.domain.repository.client

import android.util.Log
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.local.dao.ClientDao
import com.example.inventariosapp.local.entity.toModel
import com.example.inventariosapp.domain.model.client.ClientResponseModel
import com.example.inventariosapp.domain.model.client.toDb
import com.example.inventariosapp.domain.model.error.ErrorModel
import com.example.inventariosapp.domain.model.product.ProductsResponseModel
import com.example.inventariosapp.util.NetworkMonitor
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetClientsRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val clientDao: ClientDao,
    private val networkMonitor: NetworkMonitor
) {
    suspend operator fun invoke(internetUse: Boolean): Pair<List<ClientResponseModel>?, String?>{
        return if (networkMonitor.isConnected.value && internetUse) { fetchFromNetwork() }
        else { fetchFromLocal() }
    }

    private suspend fun fetchFromLocal():Pair<List<ClientResponseModel>?, String?>{
        Log.i("Client___", "call db Clients")
        try {
            val clients = clientDao.getAllClients()
            val entity = clients.map { it.toModel() }
            return Pair(entity, null)
        }
        catch (e: Exception){
            return Pair(null, e.message.toString())
        }
    }
    private suspend fun fetchFromNetwork():Pair<List<ClientResponseModel>?, String?>{
        val service = apiService.getClient()
        val response = try {
            if (service.isSuccessful) {
                withContext(Dispatchers.IO) {
                    Log.i("Client___", "update db Clients")
                    val data = service.body()?.map { it.toDb() } ?: emptyList()
                    clientDao.insertAll(data)
                }
                Pair(service.body(), null)
            }
            else {
                var error: ErrorModel
                val errorMsj = service.errorBody()?.string()
                error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                Pair(null, "Error en la peticion favor de intentar mas tarde")
            }
        }
        catch (e: Exception){ Pair(null, "Error 1001001") }
        return response
    }
    /*
    suspend operator fun invoke(internetUse: Boolean): Pair<List<ClientResponseModel>?, String?> {
        if (internetUse){
            val service = apiService.getClient()
            val response = try {
                if (service.isSuccessful) {
                    withContext(Dispatchers.IO) {
                        Log.i("Client___", "update db Clients")
                        val data = service.body()?.map { it.toDb() } ?: emptyList()
                        clientDao.insertAll(data)
                    }
                    Pair(service.body(), null)
                }
                else {
                    var error: ErrorModel
                    val errorMsj = service.errorBody()?.string()
                    error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                    Pair(null, error.MsgError?.errors.toString())
                }
            }
            catch (e: Exception){ Pair(null, e.message.toString()) }
            return response
        }
        else{
            Log.i("Client___", "call db Clients")
            try {
                val clients = clientDao.getAllClients()
                val entity = clients.map { it.toModel() }
                return Pair(entity, null)
            }
            catch (e: Exception){
                return Pair(null, e.message.toString())
            }
        }
    }
     */
}