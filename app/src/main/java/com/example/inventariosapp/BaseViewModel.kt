package com.example.inventariosapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.session.SessionManager
import com.example.inventariosapp.util.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    networkMonitor: NetworkMonitor
): ViewModel() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    // region Menu
    fun openMenu(){ MainActivity.scope.launch { MainActivity.drawerState.open() } }
    fun closeMenu(){ MainActivity.scope.launch { MainActivity.drawerState.close() } }
    // endregion
    // region Loader
    private val loader = mutableStateOf(false)
    fun showLoader(){ loader.value = true }
    fun hideLoader(){ loader.value = false }
    fun getLoader() = loader
    // endregion
    // region Internet Monitor
    var internetBtn = MutableStateFlow(true)

    var internetUses: StateFlow<Boolean> =
        combine(
            networkMonitor.isConnected,
            internetBtn
        ) { hasInternet, btnEnabled ->
            hasInternet && btnEnabled
        }
            .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
    // endregion
    // region Session
    val dialogLogin = mutableStateOf(false)
    var dialogAgain = mutableStateOf(true)
    var remaining: MutableState<Long> = mutableStateOf(Long.MIN_VALUE)
    fun observeRemainingTime() {
        viewModelScope.launch {
            while (true) {
                remaining.value = sessionManager.getRemainingTime()
                if (remaining.value <= 0) {
                    if (dialogAgain.value) {
                        logoutWithMsj()

                    }
                }
                delay(1000)
            }
        }
    }
    suspend fun isSessionValid(): Boolean {
        return sessionManager.isSessionValid()
    }
    fun startSession(
        perfilId: Int,
        usuarioId: Int,
        usuarioSesionId: Int,
        correo: String,
        nombre: String,
    ){
        viewModelScope.launch {
            sessionManager.startSession(
                perfilId,
                usuarioId,
                usuarioSesionId,
                correo,
                nombre,
            )
            dialogAgain.value = true
        }
    }

    fun logoutWithMsj(){
        viewModelScope.launch {
            sessionManager.logout()
            MainActivity.mainDialogMsg.value = "Session Expired"
            MainActivity.mainDialog.value = true
            dialogAgain.value = false
        }
    }
    fun logoutNoMsj(){
        viewModelScope.launch {
            sessionManager.logout()
        }
    }
    suspend fun getPerfilId() = sessionManager.getPerfilId()
    suspend fun getUsiarioId() = sessionManager.getUsiarioId()
    suspend fun getUsuarioSessionId() = sessionManager.getUsuarioSessionId()
    suspend fun getMail() = sessionManager.getMail()
    suspend fun getGetName() = sessionManager.getGetName()

    // endregion
    init {
        networkMonitor.start()
        // region Session
        observeRemainingTime()
        // endregion
        viewModelScope.launch {
            // region monitor internet
            snapshotFlow { networkMonitor.isConnected.value }
            networkMonitor.isConnected.collect { isConnected ->
                internetBtn.value = isConnected
            }
            // endregion
        }
    }
}
