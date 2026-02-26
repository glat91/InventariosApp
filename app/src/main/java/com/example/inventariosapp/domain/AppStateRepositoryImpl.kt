package com.example.inventariosapp.domain

import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.session.SessionManager
import com.example.inventariosapp.util.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStateRepositoryImpl @Inject constructor(
    private val sessionManager: SessionManager,
    private val networkManager: NetworkMonitor,
){
    private val _appState = MutableStateFlow<AppModeSelead>(AppModeSelead.Online)
    val appState: StateFlow<AppModeSelead> = _appState

    suspend fun regreshAppState(): AppModeSelead{
        val hasInternet = networkManager.isConnected.value
        val isSessionActive = sessionManager.isSessionValid()
        _appState.value = when {
            !hasInternet -> AppModeSelead.Offline
            hasInternet && MainActivity.internetBtn.value -> AppModeSelead.Online
            !isSessionActive -> AppModeSelead.NoSession
            else -> AppModeSelead.Session
        }
        return appState.value
    }

    fun setAppStateOffline(){
        _appState.value = AppModeSelead.Offline
    }
}