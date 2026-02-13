package com.example.inventariosapp.ui.view.login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventariosapp.BaseViewModel
import com.example.inventariosapp.MainActivity
import com.example.inventariosapp.domain.use_case.login.ValitdateUserUseCase
import com.example.inventariosapp.util.Constants
import com.example.inventariosapp.util.Helpers
import com.example.inventariosapp.util.Helpers.Companion.deletePersistKey
import com.example.inventariosapp.util.Helpers.Companion.readPersistData
import com.example.inventariosapp.util.Helpers.Companion.savePersistData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val valitdaeUserUseCase: ValitdateUserUseCase,
    val baseViewModel: BaseViewModel,
    @ApplicationContext val cnx: Context
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
    // region User Login
    fun validateUserLogin(internetUse: Boolean, onSuccess: () -> Unit = {}){
        baseViewModel.showLoader()
        viewModelScope.launch {
            if (internetUse){
                val v = valitdaeUserUseCase(uiState.user, uiState.password)
                if (v.first != null){
                    val perfilID = v.first!!.perfilId!!
                    val usuarioID = v.first!!.usuarioId!!
                    val usuarioSesionID = v.first!!.usuarioSesionId!!
                    val correo = v.first!!.correo.toString()
                    val nombre = v.first!!.nombre.toString()

                    cnx.savePersistData(perfilID, Constants.PERFIL_ID)
                    cnx.savePersistData(usuarioID, Constants.USUARIO_ID)
                    cnx.savePersistData(usuarioSesionID, Constants.USUARIO_SESION_ID)
                    cnx.savePersistData(correo, Constants.MAIL)
                    cnx.savePersistData(nombre, Constants.NOMBRE)

                    val a = cnx.readPersistData(Constants.PERFIL_ID, 0)
                    val b = cnx.readPersistData(Constants.USUARIO_ID, 0)
                    val c = cnx.readPersistData(Constants.USUARIO_SESION_ID, "")
                    val d = cnx.readPersistData(Constants.MAIL, "")
                    val e = cnx.readPersistData(Constants.NOMBRE, "")
                    Log.i("Persist___", "$a $b $c $d $e")

                    baseViewModel.startSession(
                        perfilID,
                        usuarioID,
                        usuarioSesionID,
                        correo,
                        nombre
                    )
                    delay(4000)
                    onSuccess()
                    baseViewModel.dialogLogin.value = false
                }
                else {
                    if (v.second != null) {
                        MainActivity.mainDialogMsg.value = v.second!!
                        MainActivity.mainDialog.value = true
                    }
                }
            }
            else{
                uiState.serverValidateUser = true
            }
            baseViewModel.hideLoader()
        }
    }
    // endregion
    fun saveUserLogin(){
        viewModelScope.launch {
            cnx.savePersistData(key = Constants.REMEMBER_PASSWORD, data = "${uiState.user}/${uiState.password}")
        }
    }
    fun clearUser(){
        viewModelScope.launch {
            cnx.deletePersistKey(Constants.REMEMBER_PASSWORD)
        }
    }
    fun saveBoolean(key: String, data: Boolean){
        viewModelScope.launch {
            cnx.savePersistData(key = key, data = data)
        }
    }
    // region Update Vars
    fun onUserChange(newValue: String) {
        uiState = uiState.copy(user = newValue)
    }
    fun onPasswordChange(newValue: String) {
        uiState = uiState.copy(password = newValue)
    }
    fun onRememberUserChange(newValue: Boolean) {
        uiState = uiState.copy(rememberUser = newValue)
    }
    fun onServerValidateUserChange(newValue: Boolean) {
        uiState = uiState.copy(serverValidateUser = newValue)
    }
    // endregion

    init {
        viewModelScope.launch {
            baseViewModel.showLoader()
            val session = baseViewModel.isSessionValid()
            if (session){ uiState.serverValidateUser = true }
            else{
                val data = cnx.readPersistData(key = Constants.REMEMBER_PASSWORD, default = "")
                if (!data.isBlank()){
                    val s = data.split("/")
                    onUserChange(s[0])
                    onPasswordChange(s[1])
                    onRememberUserChange(true)
                }
            }
            baseViewModel.hideLoader()
        }
    }
}

data class LoginUiState(
    var user: String = "",
    var password: String = "",
    var rememberUser: Boolean = false,
    var serverValidateUser: Boolean = false,
)