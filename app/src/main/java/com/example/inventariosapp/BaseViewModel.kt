package com.example.inventariosapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(): ViewModel() {
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
}