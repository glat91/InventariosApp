package com.example.inventariosapp.domain.repository.client

import android.util.Log
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.domain.AppModeSelead
import com.example.inventariosapp.domain.AppStateRepositoryImpl
import com.example.inventariosapp.local.dao.ClientDao
import com.example.inventariosapp.local.entity.toModel
import com.example.inventariosapp.domain.model.client.ClientResponseModel
import com.example.inventariosapp.domain.model.client.toDb
import com.example.inventariosapp.domain.model.error.ErrorModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetClientsRepositoryImp2 @Inject constructor(
    private val apiService: ApiService,
    private val clientDao: ClientDao,
    private val appStateRepository: AppStateRepositoryImpl
) {
    suspend operator fun invoke(internetUse: Boolean): Pair<List<ClientResponseModel>?, String?> {
        appStateRepository.regreshAppState()
        val appState = appStateRepository.appState
        when (appState.value) {
            AppModeSelead.NoSession, AppModeSelead.Online, AppModeSelead.Session -> {
                val service = apiService.getClient()
                val response = try {
                    if (service.isSuccessful) {
                        withContext(Dispatchers.IO) {
                            Log.i("Client___", "update db Clients")
                            val data = service.body()?.map { it.toDb() } ?: emptyList()
                            clientDao.insertAll(data)
                        }
                        Pair(service.body(), null)
                    } else {
                        var error: ErrorModel
                        val errorMsj = service.errorBody()?.string()
                        error = Gson().fromJson(errorMsj, ErrorModel::class.java)
                        Pair(null, error.MsgError?.errors.toString())
                    }
                } catch (e: Exception) {
                    Pair(null, e.message.toString())
                }
                return response
            }
            AppModeSelead.Offline -> {
                Log.i("Client___", "call db Clients")
                try {
                    val clients = clientDao.getAllClients()
                    val entity = clients.map { it.toModel() }
                    return Pair(entity, null)
                } catch (e: Exception) {
                    return Pair(null, e.message.toString())
                }
            }
        }
    }
}