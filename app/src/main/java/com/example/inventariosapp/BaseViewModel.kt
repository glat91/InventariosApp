package com.example.inventariosapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.util.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
    // endregion
    init {
        networkMonitor.start()

        viewModelScope.launch {
            networkMonitor.isConnected.collect { isConnected ->
                internetBtn.value = isConnected
            }
        }
    }
}
