package com.example.inventariosapp.domain.repository.client

import android.util.Log
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.database.dao.ClientDao
import com.example.inventariosapp.database.entity.toModel
import com.example.inventariosapp.model.client.ClientResponseModel
import com.example.inventariosapp.model.client.toDb
import com.example.inventariosapp.model.error.ErrorModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetClientsRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val clientDao: ClientDao,
) {
    suspend operator fun invoke(refresh: Boolean): Pair<List<ClientResponseModel>?, ErrorModel?> {
        if (refresh || MainActivity.internetBtn.value){
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
                    Pair(null, error)
                }
            }
            catch (e: Exception){ Pair(null, ErrorModel(error(e))) }
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
                return Pair(
                    null,
                    ErrorModel(error("Error en Base de Datos, favor de contactar a Administracion"))
                )
            }
        }

    }
}