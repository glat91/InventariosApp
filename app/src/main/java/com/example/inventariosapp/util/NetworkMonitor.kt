package com.example.inventariosapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.ui.graphics.Color
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.ui.theme.UI_Backround_Btn_Green
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isConnected.value = true
        }
        override fun onLost(network: Network) {
            _isConnected.value = false
            MainActivity.mainDialogColor.value = Color.Red
            MainActivity.mainDialogMsg.value = "Sin internet"
            MainActivity.mainDialog.value = true
        }
    }

    fun start() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, callback)
    }
    fun stop() {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}